package api.endpoint;

import api.dto.IssuesContainerDTO;
import api.dto.ProjectCustomFieldDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Represents the endpoints for ProjectCustomFieldsEndpoint.
 * This interface follows reactive and functionality paradigms.
 *
 * @author Maksym Sevriukov
 * @see reactor.core.publisher.Flux
 * @see reactor.core.publisher.Mono
 */
@RequestMapping(value = "api/project-custom-fields", produces = APPLICATION_JSON_VALUE)
public interface ProjectCustomFieldsEndpoints {
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Mono<ProjectCustomFieldDTO> create(@RequestBody Mono<ProjectCustomFieldDTO> dto);

    @GetMapping("/projects/{idp}")
    Flux<ProjectCustomFieldDTO> findAllByProjectId(@PathVariable UUID idp);

    @DeleteMapping("/{idpcf}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    Mono<Void> delete(@PathVariable Long idpcf);
}
