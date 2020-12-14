package core.service.impl;

import api.dto.ProjectDTO;

import core.entity.Project;
import core.entity.ProjectMember;
import core.exception.CustomRequestException;
import core.mapper.ProjectMapper;
import core.repository.OrganizationMemberRepository;
import core.repository.OrganizationRepository;
import core.repository.ProjectRepository;
import core.repository.ProjectRoleMemberDAO;
import core.repository.ProjectRoleMemberRepository;
import core.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRoleMemberDAO dao;
    private final ProjectRepository repository;
    private final ProjectRoleMemberRepository projectRoleMemberRepository;
    private final ProjectMapper mapper;

    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationRepository organizationRepository;


    @Transactional
    public Mono<ProjectDTO>create(Mono<ProjectDTO> projectDTOMono) {
        return projectDTOMono
                .map(projectDTO -> {
                    log.debug(" @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] -> @body: " + projectDTO );
                    return mapper.toEntity(projectDTO);
                })
                .flatMap(project -> {
                    log.debug(" @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call mapper.toEntity(projectDTO): " + project);
                    return repository
                            .findByOrganizationIdAndKey(project.getOrganizationId(), project.getKey())
                            .hasElement()
                            .flatMap(isPresent -> {
                                if(isPresent && project.getId() == null) { // project with orgId and key exist, and new project should create
                                    return Mono.error(
                                            new CustomRequestException(
                                                    "ATLAS-200: Project with this key already exist in your organization.",
                                                    HttpStatus.CONFLICT)
                                    );
                                }
                                return repository.save(project)
                                        .flatMap(saved -> {
                                            log.debug(" @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call repository.save(project): " + saved);
                                            Mono<Project> findById = repository.findById(saved.getId());
                                            ProjectMember projectRoleMember = new ProjectMember(saved.getId(), 2, saved.getLeadId());// 2 -> hard code id in table role_in_project
                                            return dao.create(projectRoleMember)
                                                    .then(findById) // switch on Mono<Project> findById
                                                    .map(found -> {
                                                        log.debug(" @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call repository.findById(saved.getId()) : " + found);
                                                        ProjectDTO projectDTO = mapper.toDTO(found);
                                                        log.debug(" @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call mapper.toDTO(found) : " + projectDTO);
                                                        return projectDTO;
                                                    });
                                        });
                            });
                });
    }

    @Transactional
    public Mono<ProjectDTO> update (Mono<ProjectDTO> projectDTOMono){
        return projectDTOMono
                .map(projectDTO -> {
                    log.debug(" @method [ Mono<ProjectDTO> update (Mono<ProjectDTO> projectDTOMono) ] -> @body: " + projectDTO );
                    return mapper.toEntity(projectDTO);
                })
                .flatMap(project -> {
                    return repository
                            .findById(project.getId())
                            .flatMap(result -> {
                                if (result.getLeadId().equals(project.getLeadId())) { // check if lead changed
                                    return updateIfLeadDoesntChanged(project);
                                }
                                else {
                                    return updateIfLeadChanged(project);
                                }
                            })
                            .switchIfEmpty(
                                    Mono.error(
                                            new CustomRequestException(
                                                    String.format("ERROR ATLAS-6: Invalid project id - %s.", project.getId()),
                                                    HttpStatus.BAD_REQUEST)
                                    )
                            );
                });
    }


    private Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) {
        return repository.save(incomingProject)
                .flatMap(saved -> {
                    log.debug(" @method [ Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) ] -> @body after @call repository.save(incomingProject): " + saved);
                    Mono<Project> findById = repository.findById(saved.getId());
                    return findById
                            .map(found -> {
                                log.debug(" @method [ Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) ] -> @body after @call repository.findById(saved.getId()) : " + found);
                                ProjectDTO projectDTO = mapper.toDTO(found);
                                log.debug(" @method [ Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) ] -> @body after @call mapper.toDTO(found) : " + projectDTO);
                                return projectDTO;
                            });
                });
    }

    private Mono<ProjectDTO> updateIfLeadChanged(Project incomingProject) {
        log.debug(" @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] -> @call repository.save(incomingProject)");
        return repository.save(incomingProject)
                .flatMap(updated -> {
                    log.debug(" @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] -> @body after @call repository.save(incomingProject): " + updated);
                    ProjectMember projectRoleMember = new ProjectMember(updated.getId(), 2, updated.getLeadId());// 2 -> hard code id in table role_in_project

                    return dao.reassignLead(projectRoleMember)
                            .then(repository.findById(updated.getId())) // call Mono<Project> findById
                            .map(found -> {
                                log.debug(" @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] -> @body after @call repository.findById(saved.getId()) : " + found);
                                ProjectDTO projectDTO = mapper.toDTO(found);
                                log.debug(" @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] -> @body after @call mapper.toDTO(found) : " + projectDTO);
                                return projectDTO;
                            });
                });
    }

    public Flux<ProjectDTO> findByUserId(String userId) {
        log.debug(String.format(" @method [ Flux<ProjectDTO> findByUserId(String userId) ] -> @param: %s", userId));
        return projectRoleMemberRepository
                .findAllByMemberId(userId)
                .flatMap(prm -> {
                    log.debug(String.format(" @method [ Flux<ProjectDTO> findByUserId(String userId) ] -> @call flatMap for each element after @call projectRoleMemberRepository.findAllByMemberId(userId);  element: [ %s ]", prm));
                    return repository.findById(prm.getProjectId()).map( project -> {
                        log.debug(String.format("  @method [ Flux<ProjectDTO> findByUserId(String userId) ] -> @body after @call repository.findById(prm.getProjectId()) for each element %s", project ));
                        ProjectDTO projectDTO = mapper.toDTO(project);
                        log.debug(String.format("  @method [ Flux<ProjectDTO> findByUserId(String userId) ] -> @body after @call mapper.toDTO(found) for each element %s", projectDTO ));
                        return projectDTO;
                    });
                });
    }
    /**
     *
     * @param oid - organization id
     * @param ovn - organization valid name
     * @param pk - project key
     *
     * */
    @Override
    public Flux<ProjectDTO> findAll(UUID oid, String ovn, String pk) {

        if (oid == null && ovn == null && pk == null) {
            return Flux.empty();
            // TODO for admins
        } else if (ovn != null && pk != null) {
            return organizationRepository.findByValidName(ovn) // find organization
                    .flatMapMany(organization -> {
                         return repository.findByOrganizationIdAndKey(organization.getId(), pk) // find project
                                 .flatMapMany(project -> {
                                    if (project.getIsPrivate()) { // if project is private check that user have permissions to access
                                        return getPrincipal() // get user principal fro token
                                                .flatMapMany( principal -> {
                                                    System.out.println(principal.getClaims());
                                                    return organizationMemberRepository
                                                            .findAllByMemberId(principal.getClaim("uid")) // get user id
                                                            .filter(organizationMember -> organizationMember.getOrganizationId().equals(organization.getId()))
                                                            .flatMap(org ->  repository.findByOrganizationIdAndKey(org.getOrganizationId(), pk).map(mapper::toDTO))
                                                            .switchIfEmpty(
                                                                    Mono.defer(() -> {
                                                                        log.error(String.format(" @method [ Flux<ProjectDTO> findAll(UUID oid, String ovn, String pk, String token) ] -> " +
                                                                                "[ ERROR ATLAS-14: You have not permissions to access this project. ] -> [ uid is %1$s ] -> [Project is %2$s ]", principal.getClaim("uid"), project));
                                                                        return Mono.error(
                                                                                new CustomRequestException(
                                                                                        "ERROR ATLAS-14: You have not permissions to access this project.",
                                                                                        HttpStatus.BAD_REQUEST)
                                                                        );
                                                                    }));
                                                });
                                    } else {
                                        return Flux.just(project).map(mapper::toDTO);
                                    }
                                });
                    })
                    .switchIfEmpty(
                            Mono.defer(() -> {
                                log.error(String.format(" @method [ Flux<ProjectDTO> findAll(UUID oid, String ovn, String pk, String token) ] -> [ ERROR ATLAS-15: Couldn't find such an organization!  - %s ]", ovn));
                                return Mono.error(
                                        new CustomRequestException(
                                                String.format("ERROR ATLAS-3: Could not find organization! - %s", ovn),
                                                HttpStatus.BAD_REQUEST)
                                );
                            })
                    );
        } else {
            return Flux.empty();
        }
    }

    private Mono<Jwt> getPrincipal() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    return (Jwt) authentication.getPrincipal();
                });
    }
}
