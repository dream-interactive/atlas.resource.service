package core.repository;

import core.entity.AtlasUser;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AtlasUserRepository extends ReactiveCrudRepository<AtlasUser, Long> {
    Mono<AtlasUser> findAllById(Long id);

}

