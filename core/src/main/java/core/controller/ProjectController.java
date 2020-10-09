package core.controller;

import api.dto.ProjectDTO;
import api.endpoint.ProjectEndpoints;
import core.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class ProjectController implements ProjectEndpoints {

    private final ProjectService service;

    public Mono<ProjectDTO> create(@RequestBody Mono<ProjectDTO> projectDTOMono) {
        return service.save(projectDTOMono);
    }

    public Mono<ProjectDTO> update(@RequestBody Mono<ProjectDTO> projectDTOMono) {
        return service.save(projectDTOMono);
    }

    @Override
    public Mono<ProjectDTO> findById(UUID id) {
        return null;
    }

    @Override
    public Flux<ProjectDTO> findByUserId(String id) {
        return service.findByUserId(id);
    }


    @Override
    public Mono<Void> delete(UUID id) {
        return null;
    }

    @Override
    public Mono<Boolean> existsByOrganizationIdAndName(UUID organizationId, String projectName) {
        return service.existsByOrganizationIdAndName(organizationId, projectName);
    }

}
