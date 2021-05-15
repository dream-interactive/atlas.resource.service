package core.dao;

import core.entity.ProjectMember;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProjectRoleMemberDAO {

    Mono<Integer> create (ProjectMember projectRoleMember);
    Mono<Integer> reassignLead (ProjectMember projectRoleMember);
    Flux<ProjectMember> findAllByProjectId(UUID idp);
}
