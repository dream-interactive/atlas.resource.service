package core.handler;

import core.entity.AtlasUser;
import core.repository.AtlasUserRepository;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AtlasUserHandlerImp {

    private final AtlasUserRepository userRepository;
        public AtlasUserHandlerImp(AtlasUserRepository userRepository) {
                this.userRepository = userRepository;
    }


    public Mono<ServerResponse> findAll(ServerRequest request) {

        Flux<AtlasUser> allUser = userRepository.findAll();
         return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allUser, AtlasUser.class);
    }
    public Mono<ServerResponse> getByUserId(ServerRequest request) {
        int userId = Integer.parseInt(request.pathVariable("id"));
        System.out.println(userId);
        Mono<ServerResponse> notfound = ServerResponse.notFound().build();
        Mono<AtlasUser> allUser = userRepository.findAtlasUserById(userId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allUser, AtlasUser.class)
                .switchIfEmpty(notfound);

    }

   /* public Mono<ServerResponse> save(ServerRequest request) {
        AtlasUser user = request.bodyToMono(AtlasUser.class).block();
        return ServerResponse.ok().build(userRepository.save(user));

    }*/

}
