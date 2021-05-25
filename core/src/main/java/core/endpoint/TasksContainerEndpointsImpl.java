package core.endpoint;

import api.dto.TaskDTO;
import api.dto.TasksContainerDTO;
import api.dto.TasksContainerTransfer;
import api.endpoint.TasksContainerEndpoints;
import core.service.TasksContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TasksContainerEndpointsImpl implements TasksContainerEndpoints {

    private final TasksContainerService service;

    @Override
    public Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> dto) {
        return service.create(dto);
    }

    @Override
    public Mono<TasksContainerDTO> update(Mono<TasksContainerDTO> dto) {
        return service.update(dto);
    }

    @Override
    public Mono<TasksContainerDTO> moveTask(Flux<TaskDTO> dto, Long currentIdtc) {
        return service.moveTask(dto, currentIdtc);
    }

    @Override
    public Flux<TasksContainerDTO> transferTask(TasksContainerTransfer transfer) {
        return service.transferTask(transfer);
    }

    @Override
    public Mono<TasksContainerDTO> findOneById(Long idic) {
        return null;
    }

    @Override
    public Flux<TasksContainerDTO> findAllByProjectId(UUID idp) {
        return service.findAllByProjectId(idp);
    }

    @Override
    public Mono<Void> delete(Long idic) {
        return service.delete(idic);
    }
}
