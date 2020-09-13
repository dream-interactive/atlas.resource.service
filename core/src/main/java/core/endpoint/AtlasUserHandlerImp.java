package core.endpoint;

import core.entity.AtlasUser;
import core.repository.AtlasUserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AtlasUserHandlerImp {


    private final AtlasUserRepository userRepository;

    public AtlasUserHandlerImp(AtlasUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Mono<ServerResponse> listUser(ServerRequest request) {
        Flux<AtlasUser> user = userRepository.getAllUsers();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(user, AtlasUser.class);
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        int userId = Integer.parseInt(request.pathVariable("id"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<AtlasUser> userMono = userRepository.getUserById(userId);
        return userMono.flatMap(user -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromObject(user)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        System.out.println("in create user");
        Mono<AtlasUser> user = request.bodyToMono(AtlasUser.class);
        return ServerResponse.ok().build(userRepository.saveUser(user));
    }
}
