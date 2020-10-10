package core.mapper;

import api.dto.ProjectDTO;
import core.entity.Project;
import core.exception.CustomRequestException;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectMapper {

    @Mapping(source = "typeId", target = "type", qualifiedByName = "typeIdToType")
    ProjectDTO toDTO (Project entity);

    @Mapping(target = "lastModify", ignore = true)
    @Mapping(source = "type", target = "typeId", qualifiedByName = "typeToTypeId")
    Project toEntity (ProjectDTO dto);

    @Named("typeToTypeId")
    default Integer typeToTypeId (String type) {
        switch (type) {
            case "SCRUM": return 1;
            case "KANBAN": return 2;
            default: throw new CustomRequestException(String.format("ERROR ATLAS-4: Invalid project type - %s", type), HttpStatus.NOT_FOUND);
        }
    }

    @Named("typeIdToType")
    default String typeIdToType (Integer typeId) {
        switch (typeId) {
            case 1: return "SCRUM";
            case 2: return "KANBAN";
            default: throw new CustomRequestException(String.format("ERROR ATLAS-5: Invalid project typeId - %d", typeId), HttpStatus.NOT_FOUND);
        }
    }

}
