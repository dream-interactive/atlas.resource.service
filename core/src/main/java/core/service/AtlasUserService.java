package core.service;

import api.dto.AtlasUserAuthDTO;
import api.dto.AtlasUserDTO;
import reactor.core.publisher.Mono;

public interface AtlasUserService {

    Mono<AtlasUserAuthDTO> create (Mono<AtlasUserAuthDTO> user);
    Mono<AtlasUserAuthDTO> updateEmailVerification(Mono<AtlasUserAuthDTO> dto);
    Mono<AtlasUserAuthDTO> findAtlasUserAuthById(String sub);

    Mono<AtlasUserDTO> update (Mono<AtlasUserDTO> user);
    Mono<AtlasUserDTO> findAtlasUserById(String sub);
}
