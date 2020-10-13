package core.service;

import api.dto.OrganizationDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrganizationService {

    Mono<OrganizationDTO> update(Mono<OrganizationDTO> organizationDTOMono);

    Mono<OrganizationDTO> create(Mono<OrganizationDTO> organizationDTOMono);

    Mono<OrganizationDTO> fetch(UUID organizationId);

    Mono<Void> delete(UUID organizationId);

    Flux<OrganizationDTO> findByUserId(String userId);

    Mono<Boolean> existByValidName(String userId);

}
