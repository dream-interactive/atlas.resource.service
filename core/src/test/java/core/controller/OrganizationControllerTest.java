package core.controller;

import api.dto.OrganizationDTO;
import core.entity.Organization;
import core.entity.OrganizationRoleMember;
import core.exception.CustomExceptionHandler;
import core.mapper.OrganizationMapper;
import core.mapper.OrganizationMapperImpl;
import core.repository.OrganizationRepository;
import core.repository.OrganizationRoleMemberDAO;
import core.repository.OrganizationRoleMemberRepository;
import core.service.impl.OrganizationServiceImpl;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Statement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import({OrganizationServiceImpl.class, OrganizationMapperImpl.class})
@WebFluxTest(OrganizationController.class)
class OrganizationControllerTest {

    @MockBean
    private OrganizationRepository repository;
    @MockBean
    private OrganizationRoleMemberRepository memberRepository;
    @MockBean
    private OrganizationRoleMemberDAO dao;

//    @Mock
//    Connection connection;
//    private DatabaseClient.Builder databaseClientBuilder;

    @Autowired
    private OrganizationMapper mapper;

    @Autowired
    private WebTestClient webTestClient;

//    @BeforeEach
//    void before() {
//        ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);
//
//        Mockito.when(connectionFactory.create()).thenReturn((Publisher) Mono.just(connection));
//        Mockito.when(connection.close()).thenReturn(Mono.empty());
//
//        databaseClientBuilder = DatabaseClient.builder()
//                .connectionFactory(connectionFactory)
//                .dataAccessStrategy(new DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE));
//    }

    @Test
    @WithMockUser
    void testUpdateOrganizationIdNull() {
        OrganizationDTO organizationDTO = new OrganizationDTO(
                null,
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationDTO), OrganizationDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void testUpdateOrganizationIdInvalid() {
        OrganizationDTO organizationDTO = new OrganizationDTO(
                UUID.fromString("eeeeeeee-bbbb-cccc-cccc-ffffffffffff"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        Mockito.when(repository.findById(organizationDTO.getId()))
                .thenReturn(Mono.empty());

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationDTO), OrganizationDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void testUpdateOrganizationIdValid() {
        OrganizationDTO organizationDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name2",
                "organization-name2",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.findByValidName("organization-name"))
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
                    Assertions.assertEquals(returnDTO.getImg(), result.getResponseBody().getImg());
                });
    }

    @Test
    @WithMockUser
    void testCreateOrganization() {

        OrganizationDTO organizationDTO = new OrganizationDTO(
                null,
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationDTO returnDTO = new OrganizationDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img"
        );

        OrganizationRoleMember organizationRoleMember = new OrganizationRoleMember(
                organizationDTO.getId(), organizationDTO.getOwnerUserId(), 1
        );

        Mockito.when(repository.findByValidName("organization-name"))
                .thenReturn(Mono.empty());
        Mockito.when(repository.save(mapper.toEntity(organizationDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(dao.create(organizationRoleMember))
                .thenReturn(Mono.just(1));
        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        // TODO fix this


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
                    Assertions.assertEquals(returnDTO.getImg(), result.getResponseBody().getImg());
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
                    Assertions.assertEquals(organizationDTO.getImg(), result.getResponseBody().getImg());
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
    void testFindByUserIdOrganization() {

        OrganizationRoleMember organizationRoleMember_1 = new OrganizationRoleMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github|13",
                1
        );
        OrganizationRoleMember organizationRoleMember_2 = new OrganizationRoleMember(
                UUID.fromString("d43405ef-ba1c-4c4b-8cfd-11f54b23113b"),
                "github|13",
                2
        );

        Organization returnOrganization_1 = new Organization(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Org Name",
                "org-name",
                "github|13",
                "img",
                null
        );
        Organization returnOrganization_2 = new Organization(
                UUID.fromString("d43405ef-ba1c-4c4b-8cfd-11f54b23113b"),
                "Org Name 2",
                "org-name-2",
                "github|77",
                "img",
                null
        );

        List<OrganizationDTO> results = List.of(
                returnOrganization_1, returnOrganization_2)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        Mockito.when(memberRepository.findAllByMemberId("github|13"))
                .thenReturn(Flux.just(organizationRoleMember_1, organizationRoleMember_2));
        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnOrganization_1));
        Mockito.when(repository.findById(UUID.fromString("d43405ef-ba1c-4c4b-8cfd-11f54b23113b")))
                .thenReturn(Mono.just(returnOrganization_2));

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/organizations?userId=github|13")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrganizationDTO.class)
                .isEqualTo(results);
    }

}