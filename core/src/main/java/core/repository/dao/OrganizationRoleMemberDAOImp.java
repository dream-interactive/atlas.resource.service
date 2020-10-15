package core.repository.dao;

import core.entity.OrganizationRoleMember;
import core.repository.OrganizationRoleMemberDAO;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class OrganizationRoleMemberDAOImp  implements OrganizationRoleMemberDAO {

   private final DatabaseClient db;

    public Mono<Integer> create(OrganizationRoleMember organizationRoleMember) {
        return db
                .execute("insert into org_role_member (organization_id, member_id, org_role_id) values (:organization_id, :member_id, :org_role_id)")
                .bind("organization_id", organizationRoleMember.getOrganizationId())
                .bind("member_id", organizationRoleMember.getMemberId())
                .bind("org_role_id", organizationRoleMember.getUserRole())
                .fetch().rowsUpdated();
    }
}
