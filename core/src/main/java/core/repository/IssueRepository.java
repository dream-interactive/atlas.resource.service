package core.repository;

import core.entity.Issue;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface IssueRepository extends R2dbcRepository<Issue, Long> {
    Flux<Issue> findAllByIdic(Long idic);
}
