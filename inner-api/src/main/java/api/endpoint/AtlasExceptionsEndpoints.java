package api.endpoint;


import api.dto.AtlasExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by Maksym Sevriukov.
 * Date: 04.12.2020
 */
@RequestMapping(value = "api/exceptions", produces = APPLICATION_JSON_VALUE)
public interface AtlasExceptionsEndpoints {

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    Flux<AtlasExceptionDTO> findAll();

}

