package core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
// import org.springframework.security.oauth2.core.OAuth2TokenValidator;
// import org.springframework.security.oauth2.jwt.*;


/**
 * Configures our application with Spring Security to restrict access to our API endpoints.
 */
@EnableWebFluxSecurity
public class SecurityConfig {

    // @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    // private String issuer;
    //
    // @Value("${auth0.audience}")
    // private String audience;



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        /*
        This is where we configure the security required for our endpoints and setup our app to serve as
        an OAuth2 Resource Server, using JWT validation.
        */
        return http
                .cors()
                .and()
                .authorizeExchange()
               // .pathMatchers("iwuerokffd").permitAll()
              //  .pathMatchers("/api/private-scoped").hasAuthority("SCOPE_read:actions")
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().and().and().build();
    }

    // @Bean
    // ReactiveJwtDecoder jwtDecoder() {
    //     /*
    //     By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
    //     indeed intended for our app. Adding our own validator is easy to do:
    //     */
    //
    //     NimbusReactiveJwtDecoder jwtDecoder = (NimbusReactiveJwtDecoder)
    //             ReactiveJwtDecoders.fromOidcIssuerLocation(issuer);
    //
    //     OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
    //     OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    //     OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator,
    //             new JwtTimestampValidator());
    //
    //     jwtDecoder.setJwtValidator(withAudience);
    //
    //     return jwtDecoder;
    // }
    //
    // private final String issuerl;
    // private final String clientId;
    //
    // public SecurityConfig(@Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}") String issuerl,
    //                       @Value("${spring.security.oauth2.client.registration.auth0.client-id}") String clientId) {
    //     this.issuerl = issuerl;
    //     this.clientId = clientId;
    // }

}
