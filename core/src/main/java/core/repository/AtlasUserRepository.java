package core.repository;

import core.entity.AtlasUser;
import org.springframework.stereotype.Repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

@Repository
public interface AtlasUserRepository extends ReactiveCrudRepository<AtlasUser, Integer> {
    Mono<AtlasUser> findAtlasUserById(Integer id);

}

