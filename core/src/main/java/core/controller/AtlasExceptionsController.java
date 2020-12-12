package core.controller;

import api.dto.AtlasExceptionDTO;
import api.endpoint.AtlasExceptionsEndpoint;
import core.service.AtlasExceptionsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class AtlasExceptionsController implements AtlasExceptionsEndpoint {

    private final AtlasExceptionsService service;

    @Override
    public Flux<AtlasExceptionDTO> findAll() {
        return service.findAll();
    }

}
