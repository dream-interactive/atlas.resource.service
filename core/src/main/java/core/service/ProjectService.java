package core.service;

import api.dto.ProjectDTO;
import core.entity.Project;
import core.exception.CustomRequestException;
import core.mapper.ProjectMapper;
import core.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    public Mono<ProjectDTO> save (Mono<ProjectDTO> projectDTOMono){
        return projectDTOMono
                .map(mapper::toEntity)
                .flatMap(project -> {
                    return repository
                            .findByOrganizationIdAndKey(project.getOrganizationId(), project.getKey())
                            .hasElement()
                            .flatMap(isPresent -> {
                                if(isPresent) {
                                    return Mono.error(
                                            new CustomRequestException(
                                                    "ERROR ATLAS-1: Project with this key already exist in your organization.",
                                                    HttpStatus.CONFLICT)
                                    );
                                }
                                return repository.save(project).map(mapper::toDTO);
                            });
                });
    }


}
