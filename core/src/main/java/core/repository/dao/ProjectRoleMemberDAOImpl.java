package core.repository.dao;

import core.entity.ProjectRoleMember;
import core.repository.ProjectRoleMemberDAO;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class ProjectRoleMemberDAOImpl implements ProjectRoleMemberDAO {

    private final DatabaseClient client;


    @Override
    public Mono<Integer> create(ProjectRoleMember projectRoleMember) {
        return client
                .execute("insert into project_role_member (project_id, role_id, member_id) values (:project_id, :role_id, :member_id)")
                .bind("project_id", projectRoleMember.getProjectId())
                .bind("role_id", projectRoleMember.getRoleId())
                .bind("member_id", projectRoleMember.getMemberId())
                .fetch().rowsUpdated();
    }

    @Override
    public Mono<Integer> reassignLead(ProjectRoleMember projectRoleMember) {

        // String unassignOldLeadSQL = String.format("update project_role_member set role_id = 3 " + // 3 -> hard code COLLABORATOR id in table role_in_project
        //         "where project_id = '%s' and role_id = 2", saved.getId()); // 2 -> hard code LEAD id in table role_in_project
//
        // String assignNewLeadSQL = String.format("insert into project_role_member (project_id, role_id, member_id) " +
        //         "values ('%s', %2$d, '%3$s') " +
        //         "on conflict on constraint project_role_member_pkey " +
        //         "do update set role_id = %2$d", saved.getId(), 2, saved.getLeadId()); // 2 -> hard code LEAD id in table role_in_project

        return client
                .execute("update project_role_member set role_id = 3 where project_id = :project_id and role_id = 2") // un-assign LEAD role
                .bind("project_id", projectRoleMember.getProjectId())
                .fetch().rowsUpdated()
                .then(
                        client
                                .execute("insert into project_role_member (project_id, role_id, member_id)" +  // assign LEAD role
                                        " values (:project_id, 2, :member_id)" +
                                        "on conflict on constraint project_role_member_pkey do update set role_id = 2")
                                .bind("project_id", projectRoleMember.getProjectId())
                                .bind("member_id", projectRoleMember.getMemberId())
                                .fetch().rowsUpdated()
                );
    }
}
