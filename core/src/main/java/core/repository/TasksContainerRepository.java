package core.repository;

import core.entity.TasksContainer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TasksContainerRepository extends ReactiveCrudRepository<TasksContainer, Long> {
    Flux<TasksContainer> findAllByIdp(UUID idp);
    Mono<Void> deleteByIdtc(Long idic);
    Mono<TasksContainer> findByIdtc(Long idic);
}
