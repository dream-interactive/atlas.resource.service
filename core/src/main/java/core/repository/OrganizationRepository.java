package core.repository;

import core.entity.Organization;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends ReactiveCrudRepository<Organization, UUID> {
    Mono<Organization> findByValidName(String validName);
//    Mono<Boolean> existsByValidName(String validName);
}
