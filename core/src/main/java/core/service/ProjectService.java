package core.service;

import api.dto.ProjectDTO;
import core.exception.CustomRequestException;
import core.mapper.ProjectMapper;
import core.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;

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
                                return repository.save(project).map(saved -> {
                                    log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call repository.save(project): " + saved);
                                    ProjectDTO projectDTO = mapper.toDTO(saved);
                                    log.debug(" @method [ Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono) ] -> @body after @call mapper.toDTO(saved) : " + projectDTO);
                                    return projectDTO;
                                });
                            });
                });
    }


}
