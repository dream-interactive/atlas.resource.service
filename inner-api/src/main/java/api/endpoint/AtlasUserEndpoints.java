package api.endpoint;


import api.dto.AtlasUserAuthDTO;
import api.dto.AtlasUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Represents the endpoints for AtlasUser.
 * This interface follows reactive and functionality paradigms.
 *
 * @author Maksym Sevriukov
 * @see reactor.core.publisher.Flux
 * @see reactor.core.publisher.Mono
 */

@RequestMapping(value = "api/users", produces = APPLICATION_JSON_VALUE)
public interface AtlasUserEndpoints {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Mono<AtlasUserAuthDTO> create(@RequestBody Mono<AtlasUserAuthDTO> dto);

    @PutMapping("/email_verification")
    @ResponseStatus(code = HttpStatus.OK)
    Mono<AtlasUserAuthDTO> updateEmailVerification(@RequestBody Mono<AtlasUserAuthDTO> dto);


    @GetMapping("/auth")
    @ResponseStatus(code = HttpStatus.OK)
    Mono<AtlasUserAuthDTO> findAtlasUserAuthById(@RequestParam("sub") String sub);

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    Mono<AtlasUserDTO> update(@RequestBody Mono<AtlasUserDTO> dto);

    @GetMapping("/{sub}")
    @ResponseStatus(code = HttpStatus.OK)
    Mono<AtlasUserDTO> findAtlasUserById(@PathVariable("sub") String sub);
}
