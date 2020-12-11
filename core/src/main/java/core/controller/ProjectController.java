package core.controller;

import api.dto.ProjectDTO;
import api.endpoint.ProjectEndpoints;
import core.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class ProjectController implements ProjectEndpoints {

    private final ProjectService service;

    public Mono<ProjectDTO> create(@RequestBody Mono<ProjectDTO> projectDTOMono) {
        return service.create(projectDTOMono);
    }

    public Mono<ProjectDTO> update(@RequestBody Mono<ProjectDTO> projectDTOMono) {
        return service.update(projectDTOMono);
    }

    @Override
    public Mono<ProjectDTO> findById(UUID id) {
        return null;
    }

    @Override
    public Flux<ProjectDTO> findByUserId(String id) {
        return service.findByUserId(id);
    }


    @Override
    public Mono<Void> delete(UUID id) {
        return null;
    }

    @Override
    public Flux<ProjectDTO> findAll(UUID oid, String ovn, String pk) {
         return service.findAll(oid, ovn, pk);
    }

    // @GetMapping("/hello")
    // Mono<OidcUser> hello(@AuthenticationPrincipal Mono<OidcUser> userMono) {
    // System.out.println("nkjnjknjkn");
    // return userMono.map(
    //     u -> {
    //       System.out.println("+++++++++++++++++" + u.getUserInfo());
    //       System.out.println("+++++++++++++++++" + u.getEmail());
    //       System.out.println("+++++++++++++++++" + u.getClaims());
    //       return u;
    //     });
    // }

}
