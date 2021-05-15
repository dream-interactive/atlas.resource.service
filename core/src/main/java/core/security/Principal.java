package core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Created by Maksym Sevriukov.
 * Date: 10.12.2020
 */

@Component
public class Principal {
    @Bean
    public Mono<Jwt> getPrincipal() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    return (Jwt) authentication.getPrincipal();
                });
    }
    @Bean
    public Mono<String> getUID() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    Jwt principal = (Jwt) authentication.getPrincipal();
                    return principal.getClaim("uid");
                });
    }
}
