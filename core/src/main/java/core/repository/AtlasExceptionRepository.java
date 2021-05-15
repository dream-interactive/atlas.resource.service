package core.repository;

import core.entity.AtlasException;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AtlasExceptionRepository extends R2dbcRepository<AtlasException, Integer> {
}

