package core.service;

import api.dto.TaskDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Mono<TaskDTO> create(Mono<TaskDTO> dto);

    Mono<TaskDTO> update(Mono<TaskDTO> dto);

    Mono<TaskDTO> findOneById(Long idi);

    Flux<TaskDTO> findAllByTasksContainer(Long idic);

    Mono<Void> delete(Long idi);
}
