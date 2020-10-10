package core.controller;

import api.dto.OrganizationDTO;
import core.entity.Organization;
import core.exception.CustomExceptionHandler;
import core.mapper.OrganizationMapper;
import core.mapper.OrganizationMapperImpl;
import core.repository.OrganizationRepository;
import core.service.OrganizationService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import({OrganizationService.class, OrganizationMapperImpl.class})
@WebFluxTest(OrganizationController.class)
class OrganizationControllerTest {

    @MockBean
    private OrganizationRepository repository;

    @Autowired
    private OrganizationMapper mapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser
    void testSaveCreateOrganization() {

        OrganizationDTO organizationDTO = new OrganizationDTO(
                null,
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.findByValidName("Organization-Name"))
                .thenReturn(Mono.empty());
        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationDTO), OrganizationDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OrganizationDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(returnDTO.getId(), Objects.requireNonNull(result.getResponseBody()).getId());
                    Assertions.assertEquals(returnDTO.getName(), result.getResponseBody().getName());
                    Assertions.assertEquals(returnDTO.getValidName(), result.getResponseBody().getValidName());
                    Assertions.assertEquals(returnDTO.getOwnerUserId(), result.getResponseBody().getOwnerUserId());
                });

    }

    @Test
    @WithMockUser
    void testSaveCreateErrorOrganization() {

        OrganizationDTO organizationDTO = new OrganizationDTO(
                null,
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        Mockito.when(repository.findByValidName("Organization-Name"))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationDTO), OrganizationDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);

    }

    @Test
    @WithMockUser
    void testSaveUpdateOrganization() {

        OrganizationDTO organizationDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.findByValidName("Organization-Name"))
                .thenReturn(Mono.just(organizationDTO).map(mapper::toEntity));
        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationDTO), OrganizationDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrganizationDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(returnDTO.getId(), Objects.requireNonNull(result.getResponseBody()).getId());
                    Assertions.assertEquals(returnDTO.getName(), result.getResponseBody().getName());
                    Assertions.assertEquals(returnDTO.getValidName(), result.getResponseBody().getValidName());
                    Assertions.assertEquals(returnDTO.getOwnerUserId(), result.getResponseBody().getOwnerUserId());
                });


    }

    @Test
    @WithMockUser
    void testFindByIdOrganization() {

        OrganizationDTO organizationDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "8d43405ef-eb60-47c9-88ed-f4a732a1eab",
                "img"
        );

        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(organizationDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/organizations/{id}", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrganizationDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(organizationDTO.getId(), Objects.requireNonNull(result.getResponseBody()).getId());
                    Assertions.assertEquals(organizationDTO.getName(), result.getResponseBody().getName());
                    Assertions.assertEquals(organizationDTO.getValidName(), result.getResponseBody().getValidName());
                    Assertions.assertEquals(organizationDTO.getOwnerUserId(), result.getResponseBody().getOwnerUserId());
                });

    }

    @Test
    @WithMockUser
    void delete() {

        Mono<Void> voidReturn = Mono.empty();

        Mockito.when(repository.deleteById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(voidReturn);

        webTestClient
                .mutateWith(csrf())
                .delete()
                .uri("/api/organizations/{id}", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"))
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    @WithMockUser
    void testExistsByValidNameOrganization() {

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        Mockito.when(repository.findByValidName("organization-name"))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/organizations?validName=organization-name")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .consumeWith(result -> Assertions.assertTrue(Objects.requireNonNull(result.getResponseBody())));

    }

    @Test
    @WithMockUser
    void testFindAllByUserIdOrganization() {

        Flux<Organization> organizationFlux = Flux.just(
                new Organization(
                        UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                        "Organization Name",
                        "Organization-Name",
                        "8d43405ef-eb60-47c9-88ed-f4a732a1eab",
                        "img",
                        null
                ),
                new Organization(
                        UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                        "Organization Name2",
                        "Organization-Name2",
                        "8d43405ef-eb60-47c9-88ed-f4a732a1eab",
                        "img",
                        null
                )
        );

        Mockito.when(repository.findAllByOwnerUserId("8d43405ef-eb60-47c9-88ed-f4a732a1eab"))
                .thenReturn(organizationFlux);

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/organizations?userId=8d43405ef-eb60-47c9-88ed-f4a732a1eab")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Organization.class)
                .isEqualTo(Objects.requireNonNull(organizationFlux.collectList().block()));
    }

}