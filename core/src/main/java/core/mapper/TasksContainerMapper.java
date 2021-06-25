package core.mapper;

import api.dto.TaskDTO;
import api.dto.TasksContainerDTO;
import core.entity.Task;
import core.entity.TasksContainer;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TasksContainerMapper {

    @Mapping(source = "tasks", target = "tasks", qualifiedByName = "toTasks")
    public abstract TasksContainer toEntity(TasksContainerDTO dto, @Context TaskMapper taskMapper);

    @Mapping(source = "tasks", target = "tasks", qualifiedByName = "toTasksDTO")
    public abstract TasksContainerDTO toDTO(TasksContainer entity, @Context TaskMapper taskMapper);

    @Named("toTasksDTO")
    protected List<TaskDTO> toTasksDTO(List<Task> tasks, @Context TaskMapper taskMapper) {
        return tasks.stream().map(taskMapper::toDTO).collect(Collectors.toList());
    }

    @Named("toTasks")
    protected List<Task> toTasks(List<TaskDTO> issues, @Context TaskMapper taskMapper) {
        return issues.stream().map(taskMapper::toEntity).collect(Collectors.toList());
    }
}
