package core.repository;

import core.entity.IssuesContainer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface IssuesContainerRepository extends ReactiveCrudRepository<IssuesContainer, Long> {
    Flux<IssuesContainer> findAllByIdp(UUID idp);

}
