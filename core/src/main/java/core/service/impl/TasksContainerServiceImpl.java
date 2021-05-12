package core.service.impl;

import api.dto.TasksContainerDTO;
import core.entity.Task;
import core.entity.TasksContainer;
import core.mapper.TaskMapper;
import core.mapper.TasksContainerMapper;
import core.repository.TaskRepository;
import core.repository.TasksContainerRepository;
import core.security.Principal;
import core.service.TasksContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TasksContainerServiceImpl implements TasksContainerService {

    private final TasksContainerRepository repository;
    private final TasksContainerMapper mapper;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final Principal principal;

    @Override
    public Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> monoDTO) {
        return principal.getUID()
                .flatMap(uid -> {
                    log.debug(String.format(
                            " @method [ Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> monoDTO) ] ->" +
                            " @user [ sub = %1$s ]", uid));
                    return monoDTO
                        .map(dto -> {
                          log.debug(String.format(
                              " @method [ Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> monoDTO) ] ->" +
                                  " @body [ dto = %1$s ]", dto));
                          TasksContainer tasksContainer = mapper.toEntity(dto, taskMapper);
                          log.debug(String.format(
                              " @method [ Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> monoDTO) ] ->" +
                                  " @body after mapper [ tasksContainer = %1$s ]", tasksContainer));
                          return tasksContainer;
                        })
                        .flatMap(tc -> repository.save(tc).map(tasksContainer -> {
                          log.debug(String.format(
                              " @method [ Mono<TasksContainerDTO> create(Mono<TasksContainerDTO> monoDTO) ] ->" +
                                  " @body after saving [ tasksContainer = %1$s ]", tasksContainer));
                          return mapper.toDTO(tasksContainer, taskMapper);
                        }));
                });
    }

    @Override
    public Mono<TasksContainerDTO> update(Mono<TasksContainerDTO> dto) {
        return null;
    }

    @Override
    public Mono<TasksContainerDTO> findOneById(Long idic) {
        return repository.findByIdtc(idic).map(container -> mapper.toDTO(container, taskMapper));
    }

    @Override
    public Flux<TasksContainerDTO> findAllByProjectId(UUID idp) {
        return repository
                .findAllByIdp(idp)
                .sort(Comparator.comparingInt(TasksContainer::getIndexNumber))
                .concatMap(container -> {
                    return taskRepository.findAllByIdtc(container.getIdtc())
                            .sort(Comparator.comparingInt(Task::getIndexNumber))
                            .collectList()
                            .map(is -> {
                                container.setTasks(is);
                                return container;
                            }).map(c -> mapper.toDTO(c, taskMapper))
                            .flux();
                });
    }

    @Override
    public Mono<Void> delete(Long idtc) {

    return principal.getUID()
        .flatMap(uid -> {
          log.debug(String.format(
              " @method [ Mono<Void> delete(Long idtc) ] -> @body [idtc = %1$s ]; @user [ sub = %2$s ]", idtc, uid));
          return repository.findByIdtc(idtc).flatMap(tasksContainer -> {
            if (tasksContainer.getCanBeDeleted()) {
              return repository.deleteByIdtc(idtc);
            } else {
              return Mono.empty();
            }
          });
        });

  }
}
