package core.router;

import core.handler.AtlasUserHandlerImp;
import core.repository.AtlasUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AtlasUserRouter {

    public AtlasUserRouter(AtlasUserRepository repository) {
        this.repository = repository;

    }
    @Autowired
    private final AtlasUserRepository repository;

    @Bean
    public RouterFunction<ServerResponse> route() {
        AtlasUserHandlerImp userHandler = new AtlasUserHandlerImp(repository);
        return RouterFunctions
                .route(GET("/users").and(accept(APPLICATION_JSON)), userHandler::findAll)
                .andRoute(GET("/users/{id}").and(accept(APPLICATION_JSON)),userHandler::getByUserId)
                .andRoute(POST("/users").and(accept(APPLICATION_JSON)),userHandler::save);
    }
}
