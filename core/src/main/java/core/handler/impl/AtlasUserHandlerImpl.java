package core.handler.impl;

import core.entity.AtlasUser;
import core.handler.AtlasUserHandler;
import core.repository.AtlasUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AtlasUserHandlerImpl implements AtlasUserHandler {

  private final AtlasUserRepository userRepository;


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

    return request
            .bodyToMono(AtlasUser.class) // Mono<AtlasUser> from request
            .flatMap(userRepository::save)  // Mono<AtlasUser> from repository
            .flatMap(user -> { // USER
              return ServerResponse
                      .created(URI.create(request.uri() + "/" + user.getId()))
                      .contentType(MediaType.APPLICATION_JSON)
                      .body(Mono.just(user), AtlasUser.class);  // Mono<AtlasUser>
            }); // Mono<ServerResponse> from repository
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
