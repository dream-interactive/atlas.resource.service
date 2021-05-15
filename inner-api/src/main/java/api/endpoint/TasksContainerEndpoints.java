package api.endpoint;

import api.dto.TasksContainerDTO;
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
 * @author Maksym Sevriukov
 * @see reactor.core.publisher.Flux
 * @see reactor.core.publisher.Mono
 */
@RequestMapping(value = "api/tasks-containers", produces = APPLICATION_JSON_VALUE)
public interface TasksContainerEndpoints {
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Mono<TasksContainerDTO> create(@RequestBody Mono<TasksContainerDTO> dto);

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    Mono<TasksContainerDTO> update(@RequestBody Mono<TasksContainerDTO> dto);

    @GetMapping("/{idic}")
    Mono<TasksContainerDTO> findOneById(@PathVariable Long idic);

    @GetMapping("/projects/{idp}")
    Flux<TasksContainerDTO> findAllByProjectId(@PathVariable UUID idp);

    @DeleteMapping("/{idic}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    Mono<Void> delete(@PathVariable Long idic);
}
