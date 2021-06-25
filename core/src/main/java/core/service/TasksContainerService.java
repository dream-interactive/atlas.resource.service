package core.service;

import api.dto.TaskDTO;
import api.dto.TasksContainerDTO;
import api.dto.TasksContainerTransfer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TasksContainerService {

    Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> dto);

    Mono<TasksContainerDTO> update(Mono<TasksContainerDTO> dto);

    Mono<TasksContainerDTO> findOneById(Long idic);

    Flux<TasksContainerDTO> findAllByProjectId(UUID idp);

    Mono<Void> delete(Long idic);

    Mono<TasksContainerDTO> moveTask(Flux<TaskDTO> tasks, Long idtc);
    Flux<TasksContainerDTO> transferTask(TasksContainerTransfer tct);
}
