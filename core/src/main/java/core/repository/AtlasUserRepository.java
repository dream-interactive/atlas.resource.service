package core.repository;

import core.entity.AtlasUser;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface AtlasUserRepository {
    Mono<AtlasUser> getUserById(int id);
    Flux<AtlasUser> getAllUsers();
    Mono<Void> saveUser(Mono<AtlasUser> user);
}
