package core.controller;

import api.dto.OrganizationDTO;
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
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.findByValidName("Organization-Name"))
                .thenReturn(Mono.empty());

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
                    Assertions.assertEquals(result.getResponseBody().getId(), returnDTO.getId());
                    Assertions.assertEquals(result.getResponseBody().getName(), returnDTO.getName());
                    Assertions.assertEquals(result.getResponseBody().getValidName(), returnDTO.getValidName());
                    Assertions.assertEquals(result.getResponseBody().getOwnerUserId(), returnDTO.getOwnerUserId());
                });

    }

    @Test
    @WithMockUser
    void testSaveCreateErrorOrganization() {

        OrganizationDTO organizationDTO = new OrganizationDTO(
                null,
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8"
        );

        Mockito.when(repository.findByValidName("Organization-Name"))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
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
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        Mockito.when(repository.findByValidName("Organization-Name"))
                .thenReturn(Mono.just(organizationDTO).map(mapper::toEntity));

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
                    Assertions.assertEquals(result.getResponseBody().getId(), returnDTO.getId());
                    Assertions.assertEquals(result.getResponseBody().getName(), returnDTO.getName());
                    Assertions.assertEquals(result.getResponseBody().getValidName(), returnDTO.getValidName());
                    Assertions.assertEquals(result.getResponseBody().getOwnerUserId(), returnDTO.getOwnerUserId());
                });


    }

    @Test
    @WithMockUser
    void testFindByIdOrganization() {

        OrganizationDTO organizationDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "Organization-Name",
                "8d43405ef-eb60-47c9-88ed-f4a732a1eab"
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
                    Assertions.assertEquals(result.getResponseBody().getId(), organizationDTO.getId());
                    Assertions.assertEquals(result.getResponseBody().getName(), organizationDTO.getName());
                    Assertions.assertEquals(result.getResponseBody().getValidName(), organizationDTO.getValidName());
                    Assertions.assertEquals(result.getResponseBody().getOwnerUserId(), organizationDTO.getOwnerUserId());
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

        // TODO finish him

        String validName = "Valid-Name";

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/organizations/exists/{validName}", validName)
                .exchange()
                .expectStatus().isOk();
//                .expectBody(Boolean.class)
//                .consumeWith(result -> {
//                    Assertions.assertEquals(result, true);
//                });

    }
}