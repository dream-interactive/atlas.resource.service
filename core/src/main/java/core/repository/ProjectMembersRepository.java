package core.repository;

import core.entity.ProjectMember;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 01.04.2021
 */
@Repository
public interface ProjectMembersRepository extends R2dbcRepository<ProjectMember, UUID> {

  Flux<ProjectMember> findAllByMemberId(String id);

  Flux<ProjectMember> findAllByProjectId(UUID idp);
}
