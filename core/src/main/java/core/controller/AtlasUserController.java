package core.controller;

import api.dto.AtlasUserAuthDTO;
import api.endpoint.AtlasUserEndpoints;
import core.service.AtlasUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class AtlasUserController implements AtlasUserEndpoints {

    private final AtlasUserService service;

    @Override
    public Mono<AtlasUserAuthDTO> create(Mono<AtlasUserAuthDTO> dto) {
        return service.create(dto);
    }

    @Override
    public Mono<AtlasUserAuthDTO> update(Mono<AtlasUserAuthDTO> dto) {
        return service.update(dto);
    }

    @Override
    public Mono<AtlasUserAuthDTO> findById(String sub) {
        return service.findById(sub);
    }
}
