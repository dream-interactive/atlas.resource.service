package core.service;

import api.dto.AtlasUserAuthDTO;
import reactor.core.publisher.Mono;

public interface AtlasUserService {

    Mono<AtlasUserAuthDTO> findById (String sub);
    Mono<AtlasUserAuthDTO> create (Mono<AtlasUserAuthDTO> user);
    Mono<AtlasUserAuthDTO> update (Mono<AtlasUserAuthDTO> user);
}
