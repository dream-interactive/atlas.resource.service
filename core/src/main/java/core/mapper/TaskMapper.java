package core.mapper;

import api.dto.TaskDTO;
import core.entity.Task;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {

    Task toEntity(TaskDTO dto);

    TaskDTO toDTO(Task entity);
}
