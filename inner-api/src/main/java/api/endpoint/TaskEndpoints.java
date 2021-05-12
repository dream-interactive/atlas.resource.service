package api.endpoint;

import api.dto.TaskDTO;
import api.dto.TaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Represents the endpoints for IssuesContainer.
 * This interface follows reactive and functionality paradigms.
 *
 * @author Maksym Sevriukov.
 * Date: 18.04.2021
 * @see reactor.core.publisher.Flux
 * @see reactor.core.publisher.Mono
 */
@RequestMapping(value = "api/tasks", produces = APPLICATION_JSON_VALUE)
public interface TaskEndpoints {

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  Mono<TaskDTO> create(@RequestBody Mono<TaskDTO> dto);

  @PutMapping
  @ResponseStatus(code = HttpStatus.OK)
  Mono<TaskDTO> update(@RequestBody Mono<TaskDTO> dto);

  @GetMapping("/{idt}")
  Mono<TaskDTO> findOneByIdt(@PathVariable Long idt);

  @GetMapping("/containers/{idtc}")
  Flux<TaskDTO> findAllByTasksContainerId(@PathVariable Long idtc);

  @DeleteMapping("/{idt}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  Mono<Void> delete(@PathVariable Long idt);


}
