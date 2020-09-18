package core.handler;

import core.entity.AtlasUser;
import core.repository.AtlasUserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class AtlasUserHandlerImp implements AtlasUserHandler {

  private final AtlasUserRepository userRepository;

  public AtlasUserHandlerImp(AtlasUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // public Mono<ServerResponse> getByUserId(ServerRequest request) {
  //    Long userId = Long.parseLong(request.pathVariable("id"));
  //    Mono<ServerResponse> notfound = ServerResponse.notFound().build();
  //    Mono<AtlasUser> allUser = userRepository.findAllById(userId);
  //    return ServerResponse
  //            .ok()
  //            .contentType(MediaType.APPLICATION_JSON)
  //            .body(allUser, AtlasUser.class)
  //            .switchIfEmpty(notfound);
  // }

  public Mono<ServerResponse> save(ServerRequest request) {

    Mono<AtlasUser> user = request
                            .bodyToMono(AtlasUser.class)
                            .flatMap(userRepository::save);

    return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(user, AtlasUser.class);
  }

  @Override
  public Mono<ServerResponse> findById(ServerRequest request) {
    return null;
  }

  @Override
  public Mono<ServerResponse> existsById(ServerRequest request) {
    return null;
  }

  @Override
  public Mono<ServerResponse> findAll(ServerRequest request) {
    Flux<AtlasUser> allUser = userRepository.findAll();
    return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(allUser, AtlasUser.class);
  }

  @Override
  public Mono<ServerResponse> deleteById(ServerRequest request) {
    return null;
  }

  @Override
  public Mono<ServerResponse> delete(ServerRequest request) {
    return null;
  }
}
