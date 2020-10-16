package core.repository;

import core.entity.OrganizationMember;
import reactor.core.publisher.Mono;

public interface OrganizationMemberDAO {
    Mono<Integer> create (OrganizationMember organizationMember);
}
