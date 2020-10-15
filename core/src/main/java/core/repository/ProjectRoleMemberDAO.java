package core.repository;

import core.entity.ProjectRoleMember;
import reactor.core.publisher.Mono;

public interface ProjectRoleMemberDAO {

    Mono<Integer> create (ProjectRoleMember projectRoleMember);
    Mono<Integer> reassignLead (ProjectRoleMember projectRoleMember);
}
