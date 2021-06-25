package api.endpoint;

import api.dto.Stat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 05.06.2021
 */
@RequestMapping(value = "api/stat", produces = MediaType.APPLICATION_JSON_VALUE)
public interface StatEndpoint {
  @GetMapping(value = "/project/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  Flux<Stat> findByProjectId(@PathVariable(value = "id") UUID id);
}
