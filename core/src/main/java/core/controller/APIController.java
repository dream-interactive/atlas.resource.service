package core.controller;

import core.entity.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class APIController {

    @GetMapping(value = "/public")
    public Mono<String> publicEndpoint() {
        return Mono.just(" All good. You DO NOT need to be authenticated to call /api/public.");
       // return Mono.just(new Message(" All good. You DO NOT need to be authenticated to call /api/public."));
    }

    @GetMapping(value = "/private")
    public Mono<Message> privateEndpoint() {
        return Mono.just(new Message("All good. You can see this because you are Authenticated."));
    }

    @GetMapping(value = "/private-scoped")
    public Mono<Message> privateScopedEndpoint() {
        return Mono.just(new Message("All good. You can see this because you are Authenticated with a Token granted the 'read:messages' scope"));
    }
}