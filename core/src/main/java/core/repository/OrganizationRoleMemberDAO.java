package core.repository;

import core.entity.OrganizationRoleMember;
import reactor.core.publisher.Mono;

public interface OrganizationRoleMemberDAO {
    Mono<Integer> create (OrganizationRoleMember organizationRoleMember);

}
