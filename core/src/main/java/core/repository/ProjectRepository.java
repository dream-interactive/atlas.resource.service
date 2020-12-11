package core.repository;

import core.entity.Project;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProjectRepository extends ReactiveCrudRepository<Project, UUID> {
    Mono<Project> findByOrganizationIdAndKey(UUID organizationId, String key);
}
