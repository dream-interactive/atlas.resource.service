package api.endpoint;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Represents the endpoints for AtlasUser.
 * This interface follows reactive and functionality paradigms.
 *
 * @author Maksym Sevriukov
 * @see RequestPredicate
 * @see ServerRequest
 */
public interface ProjectEndpoints {
    // RequestPredicate findAll = GET("/projects").and(accept(APPLICATION_JSON));
    RequestPredicate save = POST("/api/projects").and(accept(APPLICATION_JSON));
    RequestPredicate findById = GET("/api/projects/{id}").and(accept(APPLICATION_JSON));
    RequestPredicate deleteById = DELETE("/api/projects/{id}").and(accept(APPLICATION_JSON));
    RequestPredicate findAllByUserId = GET("/api/users/{id}/projects").and(accept(APPLICATION_JSON));
}
