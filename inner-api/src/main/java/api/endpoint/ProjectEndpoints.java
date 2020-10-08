package api.endpoint;

import api.dto.ProjectDTO;
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
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping(value = "api/projects", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProjectEndpoints {

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  Mono<ProjectDTO> create(@RequestBody Mono<ProjectDTO> dto);

  @PutMapping
  @ResponseStatus(code = HttpStatus.OK)
  Mono<ProjectDTO> update(@RequestBody Mono<ProjectDTO> dto);

  @GetMapping(value = "/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  Mono<ProjectDTO> findById(@PathVariable(value = "id") UUID id);

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  Mono<Void> delete(@PathVariable(value = "id") UUID id);
}
