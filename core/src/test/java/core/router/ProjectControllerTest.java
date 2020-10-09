package core.router;

import api.dto.ProjectDTO;
import core.controller.ProjectController;
import core.exception.CustomExceptionHandler;
import core.mapper.ProjectMapper;
import core.mapper.ProjectMapperImpl;
import core.repository.ProjectRepository;
import core.service.ProjectService;
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

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import({ProjectService.class, ProjectMapperImpl.class})
@WebFluxTest(ProjectController.class)
class ProjectControllerTest {
    @MockBean
    private ProjectRepository repository;

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
                1,
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);
        ProjectDTO returnDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        Mockito.when(repository.save(mapper.toEntity(projectDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));
        Mockito.when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),"PRJC"))
                .thenReturn(Mono.empty());


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
                    Assertions.assertEquals( returnDTO.getTypeId(), result.getResponseBody().getTypeId());
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
                1,
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        ProjectDTO returnDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
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
                1,
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);
        ProjectDTO returnDTO = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "ProjectName2",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|wffio3r2fjcc2v90bxi5",
                null,
                false);

        Mockito.when(repository.save(mapper.toEntity(projectDTO)))
                .thenReturn(Mono.just(returnDTO).map(mapper::toEntity));

        Mockito.when(repository.findByOrganizationIdAndKey(UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),"PRJC"))
                .thenReturn(Mono.just(projectDTO).map(mapper::toEntity));


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
                    Assertions.assertEquals( returnDTO.getTypeId(), result.getResponseBody().getTypeId());
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


}