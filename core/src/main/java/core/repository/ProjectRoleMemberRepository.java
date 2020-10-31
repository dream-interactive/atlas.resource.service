package core.repository;

import core.entity.ProjectMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ProjectRoleMemberRepository extends ReactiveCrudRepository<ProjectMember, UUID> {
    Flux<ProjectMember> findAllByMemberId(String memberId);
}
