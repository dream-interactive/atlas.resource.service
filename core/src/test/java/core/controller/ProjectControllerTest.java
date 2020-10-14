package core.controller;

import api.dto.ProjectDTO;
import core.entity.Project;
import core.entity.ProjectRoleMember;
import core.exception.CustomExceptionHandler;
import core.mapper.ProjectMapper;
import core.mapper.ProjectMapperImpl;
import core.repository.ProjectRepository;
import core.repository.ProjectRoleMemberRepository;
import core.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import({ProjectServiceImpl.class, ProjectMapperImpl.class})
@WebFluxTest(ProjectController.class)
class ProjectControllerTest {
    @MockBean
    private ProjectRepository repository;
    @MockBean
    private ProjectRoleMemberRepository projectRoleMemberRepository;
    @MockBean
    private DatabaseClient databaseClient;

    @Autowired
    private ProjectMapper mapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser
    void testSaveCreateProject() {

        ProjectDTO projectDTO = new ProjectDTO(
                null,
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);
        ProjectDTO returnDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        ProjectRoleMember projectRoleMember = new ProjectRoleMember(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"), 2, "github|wffio3r2fjcc2v90bxi5");

        Mockito.when(repository.save(mapper.toEntity(projectDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),"PRJC"))
                .thenReturn(Mono.empty());
        Mockito.when(projectRoleMemberRepository.save(projectRoleMember))
                .thenReturn(Mono.just(projectRoleMember));
        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));


        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(projectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProjectDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals( returnDTO.getId(), result.getResponseBody().getId());
                    Assertions.assertEquals( returnDTO.getName(), result.getResponseBody().getName());
                    Assertions.assertEquals( returnDTO.getKey(), result.getResponseBody().getKey());
                    Assertions.assertEquals( returnDTO.getLeadId(), result.getResponseBody().getLeadId());
                    Assertions.assertEquals( returnDTO.getType(), result.getResponseBody().getType());
                });
    }

    @Test
    @WithMockUser
    void testSaveCreateErrorProject() {

        ProjectDTO projectDTO = new ProjectDTO(
                null,
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        ProjectDTO returnDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        Mockito.when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),"PRJC"))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        Mockito.when(repository.save(mapper.toEntity(projectDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));



        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(projectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void testSaveUpdateProject() {

        ProjectDTO projectDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);
        ProjectDTO returnDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        Mockito.when(repository.save(mapper.toEntity(projectDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        Mockito.when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),"PRJC"))
                .thenReturn(Mono.just(projectDTO).map(mapper::toEntity));
        Mockito.when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(projectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProjectDTO.class)
                .consumeWith(result -> {
                    Assertions.assertEquals( returnDTO.getId(), result.getResponseBody().getId());
                    Assertions.assertEquals( returnDTO.getName(), result.getResponseBody().getName());
                    Assertions.assertEquals( returnDTO.getKey(), result.getResponseBody().getKey());
                    Assertions.assertEquals( returnDTO.getLeadId(), result.getResponseBody().getLeadId());
                    Assertions.assertEquals( returnDTO.getType(), result.getResponseBody().getType());
                });
    }
    @Test
    @WithMockUser
    void testExistsByOrganizationIdAndName() {

        Mockito.when(repository.existsByOrganizationIdAndName(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"), "ProjectName2"))
                .thenReturn(Mono.just(true));

        webTestClient
                .mutateWith(csrf())
                .get()
                .uri("/api/projects?organizationId=d43405ef-eb60-47c9-88ed-f4a732a1eab8&projectName=ProjectName2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .consumeWith(result -> Assertions.assertTrue(result.getResponseBody()));
    }

    @Test
    @WithMockUser
    void testFindByUserId() {

        ProjectRoleMember projectRoleMember =
                new ProjectRoleMember(
                        UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                        2,
                        "github|wffio3r2fjcc2v90bxi5");
        ProjectRoleMember projectRoleMember1 =
                new ProjectRoleMember(
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

        Mockito.when(projectRoleMemberRepository.findAllByMemberId("github|wffio3r2fjcc2v90bxi5"))
                .thenReturn(Flux.just(projectRoleMember, projectRoleMember1));

        Mockito.when(repository.findById(  UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.just(returnProject1));
        Mockito.when(repository.findById(  UUID.fromString("d43405ef-ba1c-4c4b-8cfd-11f54b23972e")))
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