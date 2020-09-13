package core.repository.repositoryImp;

import core.entity.AtlasUser;
import core.repository.AtlasUserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Component
public class AtlasUserRepositoryImp implements AtlasUserRepository {
    Map<Integer, AtlasUser> userMap = new ConcurrentHashMap<Integer, AtlasUser>();
    public AtlasUserRepositoryImp(){
        // adding entries to Map
        userMap.put(1, new AtlasUser(1, "Robert", "Ludlum", "rl@rl.com"));
        userMap.put(2, new AtlasUser( 2,"John", "Grisham", "jg@jg.com"));
        userMap.put(3, new AtlasUser( 3,"James", "Patterson", "jp@jp.com"));
    }

    @Override
    public Mono<AtlasUser> getUserById(int id) {
        return Mono.justOrEmpty(userMap.get(id));
    }

    @Override
    public Flux<AtlasUser> getAllUsers() {
        // get as stream
        return Flux.fromStream(userMap.values().stream());
    }

    @Override
    public Mono<Void> saveUser(Mono<AtlasUser> user) {
        Mono<AtlasUser> userMono = user.doOnNext(value->{
            userMap.put((userMap.keySet().size() +1), value);
        } );
        return userMono.then();
    }
}
