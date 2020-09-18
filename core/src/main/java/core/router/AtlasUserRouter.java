package core.router;

import api.endpoint.AtlasUserEndpoints;
import core.handler.AtlasUserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class AtlasUserRouter implements AtlasUserEndpoints {

    private final AtlasUserHandler handler;

    public AtlasUserRouter(AtlasUserHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(findAll, handler::findAll)
                .andRoute(findById, handler::findById)
                .andRoute(save, handler::save);
    }
}
