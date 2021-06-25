package core.dao.impl;

import core.dao.OrganizationMemberDAO;
import core.entity.OrganizationMember;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class OrganizationMemberDAOImp implements OrganizationMemberDAO {

   private final DatabaseClient db;

    public Mono<Integer> create(OrganizationMember organizationMember) {
        return db
                .sql("insert into org_role_member (organization_id, member_id, org_role_id) values (:organization_id, :member_id, :org_role_id)")
                .bind("organization_id", organizationMember.getOrganizationId())
                .bind("member_id", organizationMember.getMemberId())
                .bind("org_role_id", organizationMember.getUserRoleId())
                .fetch().rowsUpdated();
    }

    public Mono<Integer> update(OrganizationMember organizationMember) {
        return db
                .sql("update org_role_member set org_role_id = :org_role_id where organization_id = :organization_id and member_id = :member_id")
                .bind("organization_id", organizationMember.getOrganizationId())
                .bind("member_id", organizationMember.getMemberId())
                .bind("org_role_id", organizationMember.getUserRoleId())
                .fetch().rowsUpdated();
    }

}
