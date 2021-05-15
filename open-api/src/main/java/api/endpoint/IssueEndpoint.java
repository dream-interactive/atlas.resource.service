package api.endpoint;

import api.dto.IssueDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * Represents the endpoints for Issue.
 * This interface follows reactive and functionality paradigms.
 *
 * @author Maksym Sevriukov
 * @see reactor.core.publisher.Flux
 * @see reactor.core.publisher.Mono
 */
@RequestMapping(value = "api/issues", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IssueEndpoint {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Mono<IssueDTO> create(@RequestBody Mono<IssueDTO> dto);

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    Mono<IssueDTO> update(@RequestBody Mono<IssueDTO> dto);

    @GetMapping("/{idi}")
    Mono<IssueDTO> findOneById(@PathVariable Long idi);

    @GetMapping("/project/{idp}")
    Flux<IssueDTO> findAllByProjectId(@PathVariable UUID idp);

    @GetMapping("/container/{idic}")
    Flux<IssueDTO> findAllByIssuesContainerId(@PathVariable Long idic);

    @GetMapping("/creator/{sub}")
    Flux<IssueDTO> findAllByCreatorId(@PathVariable String sub);

    @GetMapping("/checker/{sub}")
    Flux<IssueDTO> findAllByCheckerId(@PathVariable String sub);

    @GetMapping("/assign_to_user/{sub}")
    Flux<IssueDTO> findAllByAssignToUserId(@PathVariable String sub);



    @DeleteMapping("/{idi}")
    Mono<Void> delete(@PathVariable Long idi);
}
