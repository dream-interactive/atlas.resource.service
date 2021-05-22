package core.repository;

import core.entity.Task;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepository extends R2dbcRepository<Task, Long> {
    Flux<Task> findAllByIdtc(Long idic);
}
