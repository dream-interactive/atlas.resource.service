package core.service.impl;

import api.dto.ProjectDTO;
import core.entity.Project;
import core.entity.ProjectRoleMember;
import core.exception.CustomRequestException;
import core.mapper.ProjectMapper;
import core.repository.ProjectRepository;
import core.repository.ProjectRoleMemberRepository;
import core.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final DatabaseClient db;

    private final ProjectRepository repository;
    private final ProjectRoleMemberRepository projectRoleMemberRepository;
    private final ProjectMapper mapper;


    @Transactional
    public Mono<ProjectDTO> update (Mono<ProjectDTO> projectDTOMono){
        return projectDTOMono
                .map(projectDTO -> {
                    log.debug(" @method [ Mono<ProjectDTO> update (Mono<ProjectDTO> projectDTOMono) ] -> @body: " + projectDTO );
                    return mapper.toEntity(projectDTO);
                })
                .flatMap(project -> {
                    if (project.getId() == null) {
                        return Mono.error(
                                new CustomRequestException(
                                        String.format("ERROR ATLAS-6: Invalid project id - %s.", project.getId()),
                                        HttpStatus.BAD_REQUEST)
                        );
                    }
                    else {
                        return repository
                                .findById(project.getId())
                                .flatMap(result -> {
                                    if (result.getId() == null) {
                                        return Mono.error(
                                                new CustomRequestException(
                                                        String.format("ERROR ATLAS-6: Invalid project id - %s.", result.getId()),
                                                        HttpStatus.BAD_REQUEST)
                                        );
                                    }
                                    else if (result.getLeadId().equals(project.getLeadId())) { // check if lead changed
                                        return updateIfLeadDoesntChanged(project);
                                    }
                                    else {
                                        return updateIfLeadChanged(project);
                                    }
                                });
                    }
                });
    }

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
                                                    "ERROR ATLAS-1: Project with this key already exist in your organization.",
                                                    HttpStatus.CONFLICT)
                                    );
                                }
                                return repository.save(project)
                                        .flatMap(saved -> {
                                            log.debug(" @method [ Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call repository.save(project): " + saved);
                                            Mono<Project> findById = repository.findById(saved.getId());
                                            String sql = String.format("insert into project_role_member (project_id, role_id, member_id) " +
                                                    "values ('%s', %2$d, '%3$s') ", saved.getId(), 2, saved.getLeadId()); // 2 -> hard code id in table role_in_project
                                            return executeSQL(sql)
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
                .flatMap(saved -> {

                    log.debug(" @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] -> @body after @call repository.save(incomingProject): " + saved);
                    Mono<Project> findById = repository.findById(saved.getId());

                    String unassignOldLeadSQL = String.format("update project_role_member set role_id = 3 " + // 3 -> hard code COLLABORATOR id in table role_in_project
                            "where project_id = '%s' and role_id = 2", saved.getId()); // 2 -> hard code LEAD id in table role_in_project

                    String assignNewLeadSQL = String.format("insert into project_role_member (project_id, role_id, member_id) " +
                            "values ('%s', %2$d, '%3$s') " +
                            "on conflict on constraint project_role_member_pkey " +
                            "do update set role_id = %2$d", saved.getId(), 2, saved.getLeadId()); // 2 -> hard code LEAD id in table role_in_project

                    return executeSQL(unassignOldLeadSQL) // un-assign LEAD role
                            .then(executeSQL(assignNewLeadSQL))  // assign LEAD role
                            .then(findById) // call Mono<Project> findById
                            .map(found -> {
                                log.debug(" @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] -> @body after @call repository.findById(saved.getId()) : " + found);
                                ProjectDTO projectDTO = mapper.toDTO(found);
                                log.debug(" @method [ Mono<ProjectDTO> updateIfLeadChanged (Project incomingProject) ] -> @body after @call mapper.toDTO(found) : " + projectDTO);
                                return projectDTO;
                            });
                });
    }

    private Mono<Integer> executeSQL(String sql) {
        return db.execute(sql).fetch().rowsUpdated();
    }

    public Mono<Boolean> existsByOrganizationIdAndName(UUID organizationId, String projectName) {
        log.debug(" @method [ Mono<Boolean> existsByOrganizationIdAndName(UUID organizationId, String projectName) ] -> " +
                "@params [organizationId: " + organizationId +  ", @param projectName: " + projectName + "] -> " +
                "@call repository.existsByOrganizationIdAndName(organizationId, projectName)");
        return repository.existsByOrganizationIdAndName(organizationId, projectName);
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

}
