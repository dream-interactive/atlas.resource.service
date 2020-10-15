package core.repository;

import core.entity.OrganizationRoleMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrganizationRoleMemberRepository extends ReactiveCrudRepository<OrganizationRoleMember, UUID> {
    Flux<OrganizationRoleMember> findAllByMemberId(String userId);
}
