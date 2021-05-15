package core.endpoint;

import api.dto.AtlasExceptionDTO;
import api.endpoint.AtlasExceptionsEndpoints;
import core.service.AtlasExceptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class AtlasExceptionsEndpointsImpl implements AtlasExceptionsEndpoints {

    private final AtlasExceptionsService service;

    @Override
    public Flux<AtlasExceptionDTO> findAll() {
        return service.findAll();
    }

}
