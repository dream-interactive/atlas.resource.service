package core.dao;

import core.entity.AtlasUser;
import reactor.core.publisher.Mono;

public interface AtlasUserDAO {

    Mono<Integer> create (AtlasUser user);
    Mono<Integer> update (AtlasUser user);

    Mono<Integer> updateEmailVerification (boolean emailVerification, String sub);

}
