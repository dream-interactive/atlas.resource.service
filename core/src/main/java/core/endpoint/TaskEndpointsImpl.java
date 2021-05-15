package core.endpoint;

import api.dto.TaskDTO;
import api.endpoint.TaskEndpoints;
import core.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Maksym Sevriukov.
 * Date: 18.04.2021
 */

@RestController
@RequiredArgsConstructor
public class TaskEndpointsImpl implements TaskEndpoints {

  private final TaskService service;

  @Override
  public Mono<TaskDTO> create(Mono<TaskDTO> dto) {
   // dto.subscribe(System.out::println);
    return service.create(dto);
  }

  @Override
  public Mono<TaskDTO> update(Mono<TaskDTO> dto) {
    return null;
  }

  @Override
  public Mono<TaskDTO> findOneByIdt(Long idt) {
    return null;
  }

  @Override
  public Flux<TaskDTO> findAllByTasksContainerId(Long idtc) {
    return service.findAllByTasksContainer(idtc);
  }

  @Override
  public Mono<Void> delete(Long idt) {
    return null;
  }
}
