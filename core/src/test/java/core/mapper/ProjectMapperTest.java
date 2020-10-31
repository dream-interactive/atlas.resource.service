package core.mapper;


import api.dto.ProjectDTO;
import core.entity.Project;
import core.entity.ProjectMember;
import core.exception.CustomRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@Import(ProjectMapperImpl.class)
@WebFluxTest(ProjectMapper.class)
public class ProjectMapperTest {

    @Autowired
    private ProjectMapper mapper;

    @Test
    void testToDto_returnProjectDto() {
        Project projectTypeSCRUM = new Project (
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeSCRUM",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|4",
                "null",
                false,
                null);

        Project projectTypeKANBAN = new Project (
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeKANBAN",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                2,
                "github|4",
                "null",
                false,
                null);

        Project projectTypeINVALID = new Project (
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeKANBAN",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                3,
                "github|4",
                "null",
                false,
                null);

        ProjectDTO projectDTOTypeSCRUM = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeSCRUM",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|4",
                "null",
                false);

        ProjectDTO projectDTOTypeKANBAN = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeKANBAN",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "KANBAN",
                "github|4",
                "null",
                false);

        Assertions.assertEquals(projectDTOTypeSCRUM, mapper.toDTO(projectTypeSCRUM));
        Assertions.assertEquals(projectDTOTypeKANBAN, mapper.toDTO(projectTypeKANBAN));
        Assertions.assertThrows(CustomRequestException.class, () -> mapper.toDTO(projectTypeINVALID));
    }

    @Test
    void testToEntity_returnProjectEntity() {
        Project projectTypeSCRUM = new Project (
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeSCRUM",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                1,
                "github|4",
                "null",
                false,
                null);

        Project projectTypeKANBAN = new Project (
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeKANBAN",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                2,
                "github|4",
                "null",
                false,
                null);

        ProjectDTO projectTypeDTO_INVALID = new ProjectDTO (
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeKANBAN",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "INVALID",
                "github|4",
                "null",
                false);

        ProjectDTO projectDTOTypeSCRUM = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeSCRUM",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "SCRUM",
                "github|4",
                "null",
                false);

        ProjectDTO projectDTOTypeKANBAN = new ProjectDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "projectTypeKANBAN",
                "PRJC",
                UUID.fromString("d43405ef-eb60-47c9-88ed-f4a732a1eab8"),
                "KANBAN",
                "github|4",
                "null",
                false);

        Assertions.assertEquals(projectTypeKANBAN, mapper.toEntity(projectDTOTypeKANBAN));
        Assertions.assertEquals(projectTypeSCRUM, mapper.toEntity(projectDTOTypeSCRUM));
        Assertions.assertThrows(CustomRequestException.class, () -> mapper.toEntity(projectTypeDTO_INVALID));
    }
}
