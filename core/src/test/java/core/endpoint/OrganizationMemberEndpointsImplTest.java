package core.endpoint;

import api.dto.OrganizationMemberDTO;
import core.entity.Organization;
import core.entity.OrganizationMember;
import core.exception.CustomExceptionHandler;
import core.mapper.OrganizationMemberMapper;
import core.mapper.OrganizationMemberMapperImpl;
import core.dao.OrganizationMemberDAO;
import core.repository.OrganizationMemberRepository;
import core.repository.OrganizationRepository;
import core.service.impl.OrganizationMemberServiceImpl;
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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import({OrganizationMemberServiceImpl.class, OrganizationMemberMapperImpl.class})
@WebFluxTest(OrganizationMemberEndpointsImpl.class)
class OrganizationMemberEndpointsImplTest {

    @MockBean
    private OrganizationMemberRepository repository;
    @MockBean
    private OrganizationMemberDAO dao;

    @MockBean
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMemberMapper mapper;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser
    @SuppressWarnings("unchecked")
    void testCreateOrganizationMember() {
        OrganizationMemberDTO organizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );
        OrganizationMemberDTO returnOrganizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );

        Organization organization = new Organization(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img",
                ZonedDateTime.now()
        );

        Mockito.when(repository.save(mapper.toEntity(organizationMemberDTO)))
                .thenReturn(Mono.just(returnOrganizationMemberDTO).map(mapper::toEntity));
        Mockito.when(organizationRepository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(organization));
        Mockito.when(repository.findByMemberIdAndOrganizationId("github13", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.empty(), Mono.just(returnOrganizationMemberDTO).map(mapper::toEntity));
        Mockito.when(dao.create(mapper.toEntity(organizationMemberDTO)))
                .thenReturn(Mono.just(1));

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/organizations/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationMemberDTO), OrganizationMemberDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OrganizationMemberDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(
                            returnOrganizationMemberDTO.getOrganizationId(),
                            Objects.requireNonNull(result.getResponseBody()).getOrganizationId()
                    );
                    Assertions.assertEquals(
                            returnOrganizationMemberDTO.getMemberId(),
                            result.getResponseBody().getMemberId()
                    );
                    Assertions.assertEquals(
                            returnOrganizationMemberDTO.getUserRole(),
                            result.getResponseBody().getUserRole()
                    );
                });
    }

    @Test
    @WithMockUser
    void testCreateOrganizationMemberNoMemberError() {
        OrganizationMemberDTO organizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );

        Organization organization = new Organization(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "Organization Name",
                "organization-name",
                "d43405ef-eb60-47c9-88ed-f4a732a1eab8",
                "img",
                ZonedDateTime.now()
        );

        Mockito.when(organizationRepository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(organization));
        Mockito.when(repository.findByMemberIdAndOrganizationId("github13", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(organizationMemberDTO).map(mapper::toEntity));
        Mockito.when(dao.create(mapper.toEntity(organizationMemberDTO)))
                .thenReturn(Mono.just(1));

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/organizations/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationMemberDTO), OrganizationMemberDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void testCreateOrganizationMemberNoOrganizationError() {
        OrganizationMemberDTO organizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );
        OrganizationMemberDTO returnOrganizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationMemberDTO)))
                .thenReturn(Mono.just(returnOrganizationMemberDTO).map(mapper::toEntity));
        Mockito.when(organizationRepository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.empty());

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/organizations/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationMemberDTO), OrganizationMemberDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);

    }

    @Test
    @WithMockUser
    @SuppressWarnings("unchecked")
    void testUpdateOrganizationMember() {
        OrganizationMemberDTO organizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );
        OrganizationMemberDTO returnOrganizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MANAGER"
        );

        Mockito.when(repository.save(mapper.toEntity(organizationMemberDTO)))
                .thenReturn(Mono.just(returnOrganizationMemberDTO).map(mapper::toEntity));
        Mockito.when(repository.findAllByOrganizationId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Flux.just(mapper.toEntity(returnOrganizationMemberDTO)));
        Mockito.when(repository.findByMemberIdAndOrganizationId("github13", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(mapper.toEntity(returnOrganizationMemberDTO)), Mono.just(returnOrganizationMemberDTO).map(mapper::toEntity));
        Mockito.when(dao.update(mapper.toEntity(organizationMemberDTO)))
                .thenReturn(Mono.just(1));

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/organizations/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationMemberDTO), OrganizationMemberDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrganizationMemberDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(
                            returnOrganizationMemberDTO.getOrganizationId(),
                            Objects.requireNonNull(result.getResponseBody()).getOrganizationId()
                    );
                    Assertions.assertEquals(
                            returnOrganizationMemberDTO.getMemberId(),
                            result.getResponseBody().getMemberId()
                    );
                    Assertions.assertEquals(
                            returnOrganizationMemberDTO.getUserRole(),
                            result.getResponseBody().getUserRole()
                    );
                });
    }

    @Test
    @WithMockUser
    void testUpdateOrganizationMemberNoOrganizationError() {
        OrganizationMemberDTO organizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );

        Mockito.when(repository.findAllByOrganizationId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Flux.empty());

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/organizations/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationMemberDTO), OrganizationMemberDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);

    }

    @Test
    @WithMockUser
    void testUpdateOrganizationMemberNoMemberError() {
        OrganizationMemberDTO organizationMemberDTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                "MEMBER"
        );

        Mockito.when(repository.findAllByOrganizationId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Flux.just(mapper.toEntity(organizationMemberDTO)));
        Mockito.when(repository.findByMemberIdAndOrganizationId("github13", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.empty());

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/organizations/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(organizationMemberDTO), OrganizationMemberDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);

    }

    @Test
    @WithMockUser
    void testFindByOrganizationIdOrganizationMember() {
        OrganizationMember organizationMemberDTO_1 = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github777",
                1
        );
        OrganizationMember organizationMemberDTO_2 = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                3
        );

        List<OrganizationMemberDTO> result = List.of(
                organizationMemberDTO_1, organizationMemberDTO_2)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        Mockito.when(repository.findAllByOrganizationId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Flux.just(organizationMemberDTO_1, organizationMemberDTO_2));

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/organizations/members?organizationId={organizationId}", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrganizationMemberDTO.class)
                .isEqualTo(result);
    }

    @Test
    @WithMockUser
    void testFindByMemberIdOrganizationMember() {
        OrganizationMember organizationMemberDTO_1 = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github13",
                1
        );
        OrganizationMember organizationMemberDTO_2 = new OrganizationMember(
                UUID.fromString("77777777-7777-7777-7777-11f54b23972e"),
                "github13",
                3
        );

        List<OrganizationMemberDTO> result = List.of(
                organizationMemberDTO_1, organizationMemberDTO_2)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        Mockito.when(repository.findAllByMemberId("github13"))
                .thenReturn(Flux.just(organizationMemberDTO_1, organizationMemberDTO_2));

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/organizations/members?memberId={memberId}", "github13")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrganizationMemberDTO.class)
                .isEqualTo(result);
    }

    @Test
    @WithMockUser
    void testDeleteOrganizationMember() {
        Mockito.when(repository.deleteByMemberIdAndOrganizationId("github13", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.empty());

        webTestClient
                .mutateWith(csrf())
                .delete()
                .uri("/api/organizations/members?memberId={memberId}&organizationId={organizationId}", "github13", UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"))
                .exchange()
                .expectStatus().isNoContent();
    }
}