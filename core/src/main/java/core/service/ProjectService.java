package core.service;


import api.dto.ProjectDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProjectService {

    Mono<ProjectDTO> update (Mono<ProjectDTO> projectDTOMono);
    Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono);
    Flux<ProjectDTO> findByUserId(String userId);
    Mono<Boolean> existsByOrganizationIdAndName(UUID organizationId, String projectName);

}
