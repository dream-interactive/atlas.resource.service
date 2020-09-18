package api.endpoint;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Represents the endpoints for AtlasUser.
 * This interface follows reactive and functionality paradigms.
 *
 * @author Maksym Sevriukov
 * @see RequestPredicate
 * @see ServerRequest
 */
public interface AtlasUserEndpoints {
    RequestPredicate findAll = GET("/users").and(accept(APPLICATION_JSON));
    RequestPredicate save = POST("/users").and(accept(APPLICATION_JSON));
    RequestPredicate findById = GET("/users/{id}").and(accept(APPLICATION_JSON));
}
