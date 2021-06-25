package core.service;


import api.dto.ProjectDTO;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.UUID;

public interface ProjectService {

    Mono<ProjectDTO> update (Mono<ProjectDTO> projectDTOMono);
    Mono<ProjectDTO> create (Mono<ProjectDTO> projectDTOMono);
    Flux<ProjectDTO> findByUserId(String userId);
    Flux<ProjectDTO> findAll(UUID oid, String ovn, String pk);
}
