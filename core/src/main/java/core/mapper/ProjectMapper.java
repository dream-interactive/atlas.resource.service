package core.mapper;

import api.dto.ProjectDTO;
import core.entity.Project;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ProjectMapper {

    public abstract ProjectDTO toDTO (Project entity);

    @Mapping(target = "lastModify", ignore = true)
    public abstract Project toEntity (ProjectDTO dto);

}
