package api.endpoint;

import api.dto.OrganizationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "api/organizations", produces = APPLICATION_JSON_VALUE)
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

    @GetMapping(params = "validName")
    @ResponseStatus(code = HttpStatus.OK)
    Mono<OrganizationDTO> findByValidName(@RequestParam(value = "validName") String validName);

    @GetMapping(params = "userId")
    @ResponseStatus(code = HttpStatus.OK)
    Flux<OrganizationDTO> findByUserId(@RequestParam(value = "userId") String userId);

}
