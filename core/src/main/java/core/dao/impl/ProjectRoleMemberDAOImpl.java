package core.dao.impl;

import core.dao.ProjectRoleMemberDAO;
import core.entity.ProjectMember;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class ProjectRoleMemberDAOImpl implements ProjectRoleMemberDAO {

    private final DatabaseClient client;

    @Override
    public Mono<Integer> create(ProjectMember projectRoleMember) {
        return client
                .sql("insert into project_role_member (project_id, role_id, member_id) values (:project_id, :role_id, :member_id)")
                .bind("project_id", projectRoleMember.getIdp())
                .bind("role_id", projectRoleMember.getRoleId())
                .bind("member_id", projectRoleMember.getSub())
                .fetch().rowsUpdated();
    }

    @Override
    public Mono<Integer> reassignLead(ProjectMember projectRoleMember) {

        return client
                .sql("update project_role_member set role_id = 3 where project_id = :project_id and role_id = 2") // un-assign LEAD role
                .bind("project_id", projectRoleMember.getIdp())
                .fetch().rowsUpdated()
                .then(
                        client
                                .sql("insert into project_role_member (project_id, role_id, member_id)" +  // assign LEAD role
                                        " values (:project_id, 2, :member_id)" +
                                        "on conflict on constraint project_role_member_pkey do update set role_id = 2")
                                .bind("project_id", projectRoleMember.getIdp())
                                .bind("member_id", projectRoleMember.getSub())
                                .fetch().rowsUpdated()
                );
    }

    @Override
    public Flux<ProjectMember> findAllByProjectId(UUID idp) {
        final String sql = "select project_id, member_id, role_id from project_role_member where project_id = :project_id";
        return client
                .sql(sql)
                .bind("project_id", idp)
                .fetch()
                .all()
                .cast(ProjectMember.class);
    }
}
