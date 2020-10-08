package core.router;

import api.endpoint.AtlasUserEndpoints;
import core.handler.AtlasUserHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
@AllArgsConstructor
public class AtlasUserRouter implements AtlasUserEndpoints {

    private final AtlasUserHandler handler;

    @Bean("AtlasUserRoutes")
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(findAll, handler::findAll)
                .andRoute(findById, handler::findById)
                .andRoute(save, handler::save);
    }
}
