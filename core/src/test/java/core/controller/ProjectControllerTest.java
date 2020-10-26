package core.controller;

import api.dto.ProjectDTO;
import core.entity.Project;
import core.entity.ProjectMember;
import core.exception.CustomExceptionHandler;
import core.mapper.ProjectMapper;
import core.mapper.ProjectMapperImpl;
import core.repository.ProjectRepository;
import core.repository.ProjectRoleMemberDAO;
import core.repository.ProjectRoleMemberRepository;
import core.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Assertions;
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
    @MockBean
    private ProjectRoleMemberRepository projectRoleMemberRepository;

    @MockBean
    private ProjectRoleMemberDAO dao;

    @Autowired
    private ProjectMapper mapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser
    void testCreateProject() {

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

        ProjectMember projectRoleMember = new ProjectMember(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"), 2, "github|wffio3r2fjcc2v90bxi5");

        when(repository.save(mapper.toEntity(projectDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"), "PRJC"))
                .thenReturn(Mono.empty());
        when(dao.create(projectRoleMember))
                .thenReturn(Mono.just(1));
        when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
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
                    Assertions.assertEquals(returnDTO.getId(), result.getResponseBody().getId());
                    Assertions.assertEquals(returnDTO.getName(), result.getResponseBody().getName());
                    Assertions.assertEquals(returnDTO.getKey(), result.getResponseBody().getKey());
                    Assertions.assertEquals(returnDTO.getLeadId(), result.getResponseBody().getLeadId());
                    Assertions.assertEquals(returnDTO.getType(), result.getResponseBody().getType());
                });
    }

    @Test
    @WithMockUser
    void testCreateProject_ErrorIfKeyPresent() {

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

        when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"), "PRJC"))
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
    void testUpdateProject_ErrorIfInvalidProjectId() {

        ProjectDTO projectDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        when(repository.findById(UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e")))
                .thenReturn(Mono.empty());

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(projectDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void testUpdateProject_updateIfLeadChanged() {

        ProjectDTO newDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|4",
                null,
                false);

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
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                2,
                "github|5"
        );// 2 -> hard code id in table role_in_project


        when(repository.findById(newDTO.getId()))
                .thenReturn(Mono.just(exist))
                .thenReturn(Mono.just(returned));

        when(repository.save(mapper.toEntity(newDTO)))
                .thenReturn(Mono.just(exist));

        when(dao.reassignLead(projectRoleMember))
                .thenReturn(Mono.just(1));

        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProjectDTO.class)
                .isEqualTo(mapper.toDTO(returned));
    }

    @Test
    @WithMockUser
    void testUpdateProject_updateIfLeadDoesntChanged() {

        ProjectDTO newDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|4",
                null,
                false);

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


        when(repository.findById(newDTO.getId()))
                .thenReturn(Mono.just(existDTO).map(mapper::toEntity))
                .thenReturn(Mono.just(returned));

        when(repository.save(mapper.toEntity(newDTO)))
                .thenReturn(Mono.just(existDTO).map(mapper::toEntity));


        webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newDTO), ProjectDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProjectDTO.class)
                .isEqualTo(newDTO);
    }

    @Test
    @WithMockUser
    void testFindByUserId() {

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