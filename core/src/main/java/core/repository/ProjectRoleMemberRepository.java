package core.repository;

import core.entity.ProjectRoleMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ProjectRoleMemberRepository extends ReactiveCrudRepository<ProjectRoleMember, UUID> {
    Flux<ProjectRoleMember> findAllByMemberId(String memberId);
}
