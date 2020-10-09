package api.endpoint;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import api.dto.OrganizationDTO;
import api.dto.ProjectDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.RequestPredicate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@RequestMapping(value = "api/organizations", produces = MediaType.APPLICATION_JSON_VALUE)
public interface OrganizationEndpoints {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Mono<OrganizationDTO> create(@RequestBody Mono<OrganizationDTO> dto);

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    Mono<OrganizationDTO> update(@RequestBody Mono<OrganizationDTO> dto);

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    Mono<OrganizationDTO> findById(@PathVariable(value = "id") UUID id);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    Mono<Void> delete(@PathVariable(value = "id") UUID id);

    @GetMapping(value = "/exists/{validName}")
    @ResponseStatus(code = HttpStatus.OK)
    Mono<Boolean> existByValidName(@PathVariable(value = "validName") String validName);

    @GetMapping(value = "/users/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    Flux<OrganizationDTO> findAllByUserId(@PathVariable(value = "userId") String userId);

}
