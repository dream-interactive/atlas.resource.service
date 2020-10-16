package core.repository;

import core.entity.OrganizationMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrganizationMemberRepository extends ReactiveCrudRepository<OrganizationMember, UUID> {
    Flux<OrganizationMember> findAllByMemberId(String userId);
    Flux<OrganizationMember> findAllByOrganizationId(UUID organizationId);
    Mono<Void> deleteByMemberIdAndOrganizationId(String memberId, UUID organizationId);
    Mono<OrganizationMember> findByMemberIdAndOrganizationId(String memberId, UUID organizationId);
    Flux<OrganizationMember> findAllByMemberIdAndOrganizationId(String memberId, UUID organizationId);
}
