package core.router;

import core.entity.AtlasUser;
import core.handler.AtlasUserHandlerImpl;
import core.repository.AtlasUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import(AtlasUserHandlerImpl.class)
@WebFluxTest(AtlasUserRouter.class)
class AtlasUserRouterTest {

    @MockBean
    private AtlasUserRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser
    void testSaveCreateAtlasUser() {

        AtlasUser atlasUser = new AtlasUser(
                null,
                "github|wffio3r2fjcc2v90bxi5",
                "serek57993@elesb.net",
                null);
        AtlasUser returnUser = new AtlasUser(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github|wffio3r2fjcc2v90bxi5",
                "serek57993@elesb.net",
                null);

        Mockito.when(repository.save(atlasUser)).thenReturn(Mono.just(returnUser));


        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(atlasUser), AtlasUser.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AtlasUser.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(result.getResponseBody().getId(), returnUser.getId());
                    Assertions.assertEquals(result.getResponseBody().getSub(), returnUser.getSub());
                    Assertions.assertEquals(result.getResponseBody().getEmail(), returnUser.getEmail());
                    Assertions.assertEquals(result.getResponseBody().getLastModify(), returnUser.getLastModify());
                });

        Mockito.verify(repository, times(1)).save(atlasUser);
    }

    @Test
    @WithMockUser
    void testSaveUpdateAtlasUser() {

        AtlasUser atlasUser = new AtlasUser(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github|wffio3r2fjcc2v90bxi5",
                "serek57993@elesb.net",
                null);
        AtlasUser returnUser = new AtlasUser(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github|wffio3r2fjcc2v90bxi5",
                "serek57993@elesb.net",
                null);

        Mockito.when(repository.save(atlasUser)).thenReturn(Mono.just(returnUser));


        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(atlasUser), AtlasUser.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AtlasUser.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(result.getResponseBody().getId(), returnUser.getId());
                    Assertions.assertEquals(result.getResponseBody().getSub(), returnUser.getSub());
                    Assertions.assertEquals(result.getResponseBody().getEmail(), returnUser.getEmail());
                    Assertions.assertEquals(result.getResponseBody().getLastModify(), returnUser.getLastModify());
                });

        Mockito.verify(repository, times(1)).save(atlasUser);
    }
}