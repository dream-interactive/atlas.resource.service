package core.dao;

import core.entity.OrganizationMember;
import reactor.core.publisher.Mono;

public interface OrganizationMemberDAO {
    Mono<Integer> create (OrganizationMember organizationMember);
    Mono<Integer> update (OrganizationMember organizationMember);
}
