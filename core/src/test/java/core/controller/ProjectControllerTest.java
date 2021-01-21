package core.controller;

import api.dto.ProjectDTO;
import core.entity.Project;
import core.entity.ProjectMember;
import core.exception.CustomExceptionHandler;
import core.mapper.ProjectMapper;
import core.mapper.ProjectMapperImpl;
import core.repository.OrganizationMemberRepository;
import core.repository.OrganizationRepository;
import core.repository.ProjectRepository;
import core.repository.ProjectRoleMemberDAO;
import core.repository.ProjectRoleMemberRepository;
import core.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@Import({ProjectServiceImpl.class, ProjectMapperImpl.class})
@WebFluxTest(ProjectController.class)
class ProjectControllerTest {

    @MockBean
    private ProjectRepository repository;
    @Autowired
    private ProjectMapper mapper;

    @MockBean
    private ProjectRoleMemberRepository projectRoleMemberRepository;
    @MockBean
    private ProjectRoleMemberDAO dao;

    @MockBean
    private OrganizationMemberRepository organizationMemberRepository;
    @MockBean
    private OrganizationRepository organizationRepository;


    @Autowired
    private WebTestClient webTestClient;

    ProjectDTO incomingProjectDTO = new ProjectDTO();
    ProjectDTO returnProjectDTO = new ProjectDTO();

    @BeforeEach
    void init() {
        incomingProjectDTO.setId(null);
        incomingProjectDTO.setName("ProjectName");
        incomingProjectDTO.setKey("PRJCT");
        incomingProjectDTO.setOrganizationId(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"));
        incomingProjectDTO.setType("SCRUM");
        incomingProjectDTO.setLeadId("wffio3r2fjcc2v90bxi5");
        incomingProjectDTO.setImg("img");
        incomingProjectDTO.setIsPrivate(true);


        returnProjectDTO.setId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"));
        returnProjectDTO.setName("ProjectName");
        returnProjectDTO.setKey("PRJCT");
        returnProjectDTO.setOrganizationId(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"));
        returnProjectDTO.setType("SCRUM");
        returnProjectDTO.setLeadId("wffio3r2fjcc2v90bxi5");
        returnProjectDTO.setImg("img");
        returnProjectDTO.setIsPrivate(true);

    }


    @Test
    @WithMockUser
    void create_correctProjectDTO_201_projectDTO() {

        ProjectMember projectRoleMember = new ProjectMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                2,
                "wffio3r2fjcc2v90bxi5");

        when(repository.save(mapper.toEntity(incomingProjectDTO)))
                .thenReturn(Mono.just(returnProjectDTO).map(mapper::toEntity));
        when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"), "PRJCT"))
                .thenReturn(Mono.empty());
        when(dao.create(projectRoleMember))
                .thenReturn(Mono.just(1));
        when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnProjectDTO).map(mapper::toEntity));


        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingProjectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProjectDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals(returnProjectDTO.getId(), result.getResponseBody().getId());
                    Assertions.assertEquals(returnProjectDTO.getName(), result.getResponseBody().getName());
                    Assertions.assertEquals(returnProjectDTO.getKey(), result.getResponseBody().getKey());
                    Assertions.assertEquals(returnProjectDTO.getLeadId(), result.getResponseBody().getLeadId());
                    Assertions.assertEquals(returnProjectDTO.getType(), result.getResponseBody().getType());
                });
    }

    @Test
    @WithMockUser
    void create_incomingProjectWithIdNotNULL_400_BadRequest() {
        incomingProjectDTO.setId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"));

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingProjectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithMockUser
    void create_correctProjectWithKeyPresent_409_CONFLICT() {

        when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"), "PRJCT"))
                .thenReturn(Mono.just(returnProjectDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingProjectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus()
                .reasonEquals("Conflict")
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void update_invalidProjectId_404CustomRequestException() {
        incomingProjectDTO.setId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"));

        when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.empty());

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingProjectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void update_ifLeadChanged_200_projectDTO() {

        incomingProjectDTO.setId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"));

        Project exist = new Project(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|5",
                null,
                false,
                null);

        Project returned = new Project (
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|4",
                null,
                false,
                null);

        ProjectMember projectRoleMember = new ProjectMember(
                incomingProjectDTO.getId(),
                2,
                "github|4"
        );// 2 -> hard code id in table role_in_project


        when(repository.findById(incomingProjectDTO.getId()))
                .thenReturn(Mono.just(exist))
                .thenReturn(Mono.just(returned));

        when(repository.save(mapper.toEntity(incomingProjectDTO)))
                .thenReturn(Mono.just(returned));

        when(dao.reassignLead(projectRoleMember))
                .thenReturn(Mono.just(1));

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingProjectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProjectDTO.class)
                .isEqualTo(mapper.toDTO(returned));
    }
/*TODO fix tests below*/
    @Test
    @WithMockUser
    void update_ifLeadDoesntChanged_200_projectDTO() {

        incomingProjectDTO.setId(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"));

        ProjectDTO existDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|4",
                null,
                false);

        Project returned = new Project(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|4",
                null,
                false,
                null);


        when(repository.findById(incomingProjectDTO.getId()))
                .thenReturn(Mono.just(existDTO).map(mapper::toEntity))
                .thenReturn(Mono.just(returned));

        when(repository.save(mapper.toEntity(incomingProjectDTO)))
                .thenReturn(Mono.just(existDTO).map(mapper::toEntity));


        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingProjectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProjectDTO.class)
                .isEqualTo(incomingProjectDTO);
    }

    @Test
    @WithMockUser
    void findByUserId_400_CustomRequestException() {

        ProjectMember projectRoleMember =
                new ProjectMember(
                        UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                        2,
                        "github|wffio3r2fjcc2v90bxi5");
        ProjectMember projectRoleMember1 =
                new ProjectMember(
                        UUID.fromString("d43405ef-ba1c-4c4b-8cfd-11f54b23972e"),
                        2,
                        "github|wffio3r2fjcc2v90bxi5");


        Project returnProject1 = new Project(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false,
                null);

        Project returnProject2 = new Project(
                UUID.fromString("d43405ef-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC2",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false,
                null);

        List<ProjectDTO> results = List.of(returnProject1, returnProject2).stream().map(mapper::toDTO).collect(Collectors.toList());

        when(projectRoleMemberRepository.findAllByMemberId("github|wffio3r2fjcc2v90bxi5"))
                .thenReturn(Flux.just(projectRoleMember, projectRoleMember1));

        when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnProject1));
        when(repository.findById(UUID.fromString("d43405ef-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnProject2));

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/projects?userId=github|wffio3r2fjcc2v90bxi5")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProjectDTO.class)
                .isEqualTo(results);

    }


}