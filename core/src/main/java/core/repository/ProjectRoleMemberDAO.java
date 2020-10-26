package core.repository;

import core.entity.ProjectMember;
import reactor.core.publisher.Mono;

public interface ProjectRoleMemberDAO {

    Mono<Integer> create (ProjectMember projectRoleMember);
    Mono<Integer> reassignLead (ProjectMember projectRoleMember);
}
