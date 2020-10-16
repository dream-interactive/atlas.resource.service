package core.service;

import api.dto.OrganizationMemberDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrganizationMemberService {

    Mono<OrganizationMemberDTO> create(Mono<OrganizationMemberDTO> organizationMemberDTOMono);

    Mono<OrganizationMemberDTO> update(Mono<OrganizationMemberDTO> organizationMemberDTOMono);

    Flux<OrganizationMemberDTO> findByOrganizationId(UUID organizationId);

    Flux<OrganizationMemberDTO> findByMemberId(String memberId);

    Mono<Void> delete(String memberId, UUID organizationId);

}
