package core.handler;

import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface for generic CRUD operations on a handler for a specific type. This handler follows reactive paradigms
 * and uses Project Reactor types which are built on top of Reactive Streams.
 *
 * @author Maksym Sevriukov
 * @see Mono
 * @see Flux
 */

public interface CrudHandler {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param request must contain body with {@literal entity}.
     * @return {@link Mono<ServerResponse>} emitting the saved entity.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}. // TODO
     */
   Mono<ServerResponse> save(ServerRequest request);

    /**
     * Retrieves an entity by its id.
     *
     * @param request must contain body with field {@literal id}.
     * @return {@link Mono<ServerResponse>} emitting the entity with the given id or {@link Mono#empty()} if none found.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}.
     */
    Mono<ServerResponse> findById(ServerRequest request);

    /**
     * Returns whether an entity with the given {@literal id} exists.
     *
     * @param request must contain body with field {@literal id}.
     * @return {@link Mono<ServerResponse>} emitting {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}.
     */
    Mono<ServerResponse> existsById(ServerRequest request);

    /**
     * Returns all instances of the type.
     *
     * @return  {@link Mono<ServerResponse>} emitting {@link Flux } with all entities.
     */
    Mono<ServerResponse> findAll(ServerRequest request);

    /**
     * Deletes the entity with the given id.
     *
     * @param request must contain body with field {@literal id}.
     * @return {@link Mono<ServerResponse>} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}.
     */
    Mono<ServerResponse> deleteById(ServerRequest request);

    /**
     * Deletes a given entity.
     *
     * @param request must contain body with {@literal entity}.
     * @return {@link Mono<ServerResponse>} signaling when operation has completed.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Mono<ServerResponse> delete(ServerRequest request);

}
