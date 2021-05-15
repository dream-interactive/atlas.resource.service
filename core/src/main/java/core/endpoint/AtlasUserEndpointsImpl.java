package core.endpoint;

import api.dto.AtlasUserAuthDTO;
import api.dto.AtlasUserDTO;
import api.endpoint.AtlasUserEndpoints;
import core.service.AtlasUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AtlasUserEndpointsImpl implements AtlasUserEndpoints {

    private final AtlasUserService service;

    @Override
    public Mono<AtlasUserAuthDTO> create(Mono<AtlasUserAuthDTO> dto) {
        return service.create(dto);
    }

    @Override
    public Mono<AtlasUserAuthDTO> updateEmailVerification(Mono<AtlasUserAuthDTO> dto) {
        return service.updateEmailVerification(dto);
    }

    @Override
    public Mono<AtlasUserAuthDTO> findAtlasUserAuthById(String sub) {
        return service.findAtlasUserAuthById(sub);
    }

    @Override
    public Mono<AtlasUserDTO> update(Mono<AtlasUserDTO> dto) {
        return service.update(dto);
    }

    @Override
    public Mono<AtlasUserDTO> findAtlasUserById(String sub) {
        return service.findAtlasUserById(sub);
    }

}
