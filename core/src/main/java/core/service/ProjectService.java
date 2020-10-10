package core.service;

import api.dto.ProjectDTO;
import core.entity.Project;
import core.entity.ProjectRoleMember;
import core.exception.CustomRequestException;
import core.mapper.ProjectMapper;
import core.repository.ProjectRepository;
import core.repository.ProjectRoleMemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectRoleMemberRepository projectRoleMemberRepository;
    private final ProjectMapper mapper;

    // @formatter:off
    public Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono){

        return projectDTOMono
                .map(projectDTO -> {
                    log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @body: " + projectDTO );
                    return mapper.toEntity(projectDTO);
                })
                .flatMap(project -> {
                    log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call mapper.toEntity(projectDTO): " + project);
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
                                log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @call repository.save(project)");
                                return repository.save(project)
                                        .flatMap(saved -> {
                                            log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call repository.save(project): " + saved);
                                            Mono<Project> findById = repository.findById(saved.getId());
                                            return findById.map(found -> {
                                                log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call repository.findById(saved.getId()) : " + found);
                                                ProjectDTO projectDTO = mapper.toDTO(found);
                                                log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call mapper.toDTO(found) : " + projectDTO);
                                                return projectDTO;
                                            });
                                        });
                            });
                });
    }
    // @formatter:on
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
