package core.repository;

import core.entity.OrganizationRoleMember;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class OrganizationRoleMemberDAO {

    DatabaseClient db;

    public Mono<Integer> create(OrganizationRoleMember organizationRoleMember) {
        return db
                .insert()
                .into(OrganizationRoleMember.class)
                .using(organizationRoleMember)
                .fetch()
                .rowsUpdated();
    }
}
