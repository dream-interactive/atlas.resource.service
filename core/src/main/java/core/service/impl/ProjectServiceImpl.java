package core.service.impl;

import api.dto.ProjectDTO;
import core.dao.TaskDAO;
import core.entity.TasksContainer;
import core.entity.Organization;
import core.entity.Project;
import core.entity.ProjectMember;
import core.exception.CustomRequestException;
import core.mapper.ProjectMapper;
import core.repository.TaskRepository;
import core.repository.TasksContainerRepository;
import core.repository.OrganizationMemberRepository;
import core.repository.OrganizationRepository;
import core.repository.ProjectRepository;
import core.dao.ProjectRoleMemberDAO;
import core.repository.ProjectMembersRepository;
import core.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

import static reactor.core.publisher.Flux.zip;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    private final ProjectMembersRepository projectRoleMemberRepository;
    private final ProjectRoleMemberDAO projectRoleMemberDAO;

    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationRepository organizationRepository;

    private final TasksContainerRepository icRepository;
    private final TaskRepository taskRepository;
    private final TaskDAO taskDAO;


    @Transactional
    public Mono<ProjectDTO>create(Mono<ProjectDTO> projectDTOMono) {
        return projectDTOMono
                .map(projectDTO -> {
                    log.debug(
                            " @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] ->" +
                            " @body: "                                                                  +
                            projectDTO );

                    if(projectDTO.getIdp() != null) {
                        throw new CustomRequestException("ID should be null", HttpStatus.BAD_REQUEST);
                    }
                    return mapper.toEntity(projectDTO);
                })
                .flatMap(project -> {
                    log.debug(
                            " @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] ->" +
                            " @body after @call mapper.toEntity(projectDTO): "                          +
                            project);
                    return repository
                            .findByOrganizationIdAndKey(project.getOrganizationId(), project.getKey())
                            .hasElement()
                            .flatMap(isPresent -> {
                                if(isPresent && project.getIdp() == null) { // project with orgId and key exist, and new project should create
                                    return Mono.error(
                                            new CustomRequestException(
                                                    "ATLAS-200: Project with this key already exist in your organization.",
                                                    HttpStatus.CONFLICT)
                                    );
                                }
                                return repository.save(project)
                                        .flatMap(saved -> {
                                            log.debug(
                                                    " @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] ->" +
                                                    " @body after @call repository.save(project): "                             +
                                                    saved);
                                            Mono<Project> findById = repository.findById(saved.getIdp());
                                            ProjectMember projectRoleMember = new ProjectMember(saved.getIdp(), 2, saved.getLeadId());// 2 -> hard code id in table role_in_project

                                            Mono<Integer> prmId$ = projectRoleMemberDAO.create(projectRoleMember);

                                            TasksContainer todo = new TasksContainer(
                                                    "To Do",
                                                    project.getIdp(),
                                                    true,
                                                    new ArrayList<>(),
                                                    0
                                            );

                                            TasksContainer inWork = new TasksContainer(
                                                "In Work",
                                                    project.getIdp(),
                                                    true,
                                                    new ArrayList<>(),
                                                    1
                                            );
                                            TasksContainer done = new TasksContainer(
                                                    "Done",
                                                    project.getIdp(),
                                                    false,
                                                    new ArrayList<>(),
                                                    2
                                            );
                                            Mono<TasksContainer> work$ = icRepository.save(inWork);
                                            Mono<TasksContainer> todo$ = icRepository.save(todo);
                                            Mono<TasksContainer> done$ = icRepository.save(done);

                                            return Mono.zip(prmId$, work$, todo$, done$)
                                                    .then(findById) // switch on Mono<Project> findById
                                                    .map(found -> {
                                                        log.debug(
                                                                " @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] ->" +
                                                                " @body after @call repository.findById(saved.getId()) : "                  +
                                                                found);
                                                        ProjectDTO projectDTO = mapper.toDTO(found);
                                                        log.debug(
                                                                " @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] ->" +
                                                                " @body after @call mapper.toDTO(found) : "                                 +
                                                                projectDTO);
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
                    log.debug(
                            " @method [ Mono<ProjectDTO> update (Mono<ProjectDTO> projectDTOMono) ] ->"   +
                            " @body: "                                                                    +
                            projectDTO );
                    return mapper.toEntity(projectDTO);
                })
                .flatMap(project -> {
                    return repository
                            .findById(project.getIdp())
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
                                                    "ATLAS-901: Could not find project.",
                                                    HttpStatus.BAD_REQUEST)
                                    )
                            );
                });
    }

    private Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) {

        Assert.notNull(incomingProject, "Project can't be null");

        return repository.save(incomingProject)
                .flatMap(saved -> {
                    log.debug(
                            " @method [ Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) ] ->" +
                            " @body after @call repository.save(incomingProject): "                                +
                            saved);
                    Mono<Project> findById = repository.findById(saved.getIdp());
                    return findById
                            .map(found -> {
                                log.debug(
                                        " @method [ Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) ] ->" +
                                        " @body after @call repository.findById(saved.getId()) : "                             +
                                        found);
                                ProjectDTO projectDTO = mapper.toDTO(found);
                                log.debug(
                                        " @method [ Mono<ProjectDTO> updateIfLeadDoesntChanged (Project incomingProject) ] ->" +
                                        " @body after @call mapper.toDTO(found) : "                                            +
                                        projectDTO);
                                return projectDTO;
                            });
                });
    }

    private Mono<ProjectDTO> updateIfLeadChanged(Project incomingProject) {

        Assert.notNull(incomingProject, "Project can't be null");

        return repository.save(incomingProject)
                .flatMap(updated -> {
                    log.debug(
                            " @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] ->" +
                            " @body after @call repository.save(incomingProject): "                          +
                            updated);
                    ProjectMember projectRoleMember = new ProjectMember(updated.getIdp(), 2, updated.getLeadId());// 2 -> hard code id in table role_in_project

                    return projectRoleMemberDAO.reassignLead(projectRoleMember)
                            .then(repository.findById(updated.getIdp())) // call Mono<Project> findById
                            .map(found -> {
                                log.debug(
                                        " @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] ->" +
                                        " @body after @call repository.findById(saved.getId()) : "                       +
                                        found);
                                ProjectDTO projectDTO = mapper.toDTO(found);
                                log.debug(
                                        " @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] ->" +
                                        " @body after @call mapper.toDTO(found) : "                                      +
                                        projectDTO);
                                return projectDTO;
                            });
                });
    }

    public Flux<ProjectDTO> findByUserId(String userId) {

        if (userId.trim().isEmpty()) {
            return Flux.error(
                    new CustomRequestException("ATLAS-900: UserId can't be null or empty.", HttpStatus.BAD_REQUEST)
            );
        }

        return projectRoleMemberRepository
                .findAllBySub(userId)
                .flatMap(prm -> {
                    log.debug(String.format(
                            " @method [ Flux<ProjectDTO> findByUserId(String userId) ] ->"                                                       +
                            " @call flatMap for each element after @call projectRoleMemberRepository.findAllByMemberId(userId);  element: [ %s ]",
                            prm));
                    return
                        repository
                        .findById(prm.getIdp())
                        .map( project -> {
                          log.debug(String.format(
                                  " @method [ Flux<ProjectDTO> findByUserId(String userId) ] ->"                      +
                                  " @body after @call repository.findById(prm.getProjectId()) for each element %s"    ,
                                  project ));
                        return mapper.toDTO(project);
                        }).flatMap(dto ->
                            taskDAO
                                .findLabelsByIdp(dto.getIdp())
                                .collectList()
                                .map(labels -> { // set labels
                                      dto.setLabels(labels);
                                      return dto;
                                }));
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
            if (ovn.trim().isEmpty() || pk.trim().isEmpty()) {
                return Flux.error(
                        new CustomRequestException(
                                "ATLAS-900: Organization valid name or project key can't be empty.",
                                HttpStatus.BAD_REQUEST)
                );
            }

          return getProjectByOvnAndPK(ovn, pk);
        } else {
            return Flux.empty();
        }
    }

  private Flux<ProjectDTO> getProjectByOvnAndPK(String ovn, String pk) {
    return organizationRepository.findByValidName(ovn) // find organization
            .flatMapMany(organization -> {
                 return repository.findByOrganizationIdAndKey(organization.getId(), pk) // find project
                         .flatMapMany(project -> {
                            if (project.getIsPrivate()) { // if project is private check that user have permissions to access
                                return ifProjectIsPrivate(pk, organization, project);
                            } else {
                                return Flux.just(project).map(mapper::toDTO);
                            }
                         })
                         .flatMap(dto -> taskDAO.findLabelsByIdp(dto.getIdp()).collectList().map(labels -> {
                           dto.setLabels(labels);
                           return dto;
                         }))
                         .switchIfEmpty(
                             Mono.error(
                                     new CustomRequestException(
                                             "ATLAS-901: Could not find project.",
                                             HttpStatus.BAD_REQUEST))
                         );
            })
            .switchIfEmpty(
                Mono.error(
                    new CustomRequestException(
                            "ATLAS-901: Could not find organization.",
                            HttpStatus.BAD_REQUEST))
            );
  }

  private Flux<ProjectDTO> ifProjectIsPrivate(String pk, Organization organization, Project project) {
        return getPrincipal() // get user principal from token
                .flatMapMany( principal -> {
                    return organizationMemberRepository
                        .findAllByMemberId(principal.getClaim("uid")) // get user id
                        .filter(organizationMember -> organizationMember.getOrganizationId().equals(organization.getId()))
                        .flatMap(org ->  repository.findByOrganizationIdAndKey(org.getOrganizationId(), pk).map(mapper::toDTO))
                        .switchIfEmpty(
                            Mono.error(() -> {
                                log.error(String.format(
                                    " @method [ Flux<ProjectDTO> findAll(UUID oid, String ovn, String pk, String token) ] ->"   +
                                    " ATLAS-201: You do not have permission to access this project. ->"                         +
                                    " @data [ uid = %1$s; project = %2$s ]"                                                     ,
                                    principal.getClaim("uid")                                                                   ,
                                    project));
                                return new CustomRequestException(
                                    "ATLAS-201: You do not have permission to access this project.",
                                    HttpStatus.FORBIDDEN);
                            })
                        );
                });
    }

    private Mono<Jwt> getPrincipal() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    return (Jwt) authentication.getPrincipal();
                });
    }

}
