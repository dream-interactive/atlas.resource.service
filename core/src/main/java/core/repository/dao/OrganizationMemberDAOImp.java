package core.repository.dao;

import core.entity.OrganizationMember;
import core.repository.OrganizationMemberDAO;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class OrganizationMemberDAOImp implements OrganizationMemberDAO {

   private final DatabaseClient db;

    public Mono<Integer> create(OrganizationMember organizationMember) {
        return db
                .execute("insert into org_role_member (organization_id, member_id, org_role_id) values (:organization_id, :member_id, :org_role_id)")
                .bind("organization_id", organizationMember.getOrganizationId())
                .bind("member_id", organizationMember.getMemberId())
                .bind("org_role_id", organizationMember.getUserRoleId())
                .fetch().rowsUpdated();
    }

}
