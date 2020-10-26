package core.repository;

import core.entity.AtlasUser;
import core.entity.ProjectMember;
import reactor.core.publisher.Mono;

public interface AtlasUserDAO {

    Mono<Integer> create (AtlasUser user);

}
