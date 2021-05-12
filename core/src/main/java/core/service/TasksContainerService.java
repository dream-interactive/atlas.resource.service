package core.service;

import api.dto.TasksContainerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TasksContainerService {

    Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> dto);

    Mono<TasksContainerDTO> update(Mono<TasksContainerDTO> dto);

    Mono<TasksContainerDTO> findOneById(Long idic);

    Flux<TasksContainerDTO> findAllByProjectId(UUID idp);

    Mono<Void> delete(Long idic);
}
