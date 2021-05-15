package core.service;

import api.dto.AtlasExceptionDTO;
import reactor.core.publisher.Flux;

public interface AtlasExceptionsService {

    Flux<AtlasExceptionDTO> findAll();
}
