package core.service.impl;

import api.dto.TaskDTO;
import core.entity.Task;
import core.mapper.TaskMapper;
import core.repository.TaskRepository;
import core.security.Principal;
import core.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final Principal principal;

    @Override
    public Mono<TaskDTO> create(Mono<TaskDTO> dto) {
        return principal.getUID()
            .flatMap(sub -> {
                log.debug(String.format(
                    " @method [ Mono<TaskDTO> create(Mono<TaskDTO> dto) ] ->" +
                    " @user [ sub = %1$s ]", sub));
                return dto
                    .map(taskDTO -> {
                        log.debug(String.format(
                            " @method [ Mono<TaskDTO> create(Mono<TaskDTO> dto) ] ->" +
                            " @body [ dto = %1$s ]", taskDTO));
                        Task task = mapper.toEntity(taskDTO);
                        log.debug(String.format(
                            " @method [ Mono<TaskDTO> create(Mono<TaskDTO> dto) ] ->" +
                            " @body after mapper [ task = %1$s ]", task));
                        return task;
                    })
                    .flatMap(task -> repository
                        .save(task)
                        .map(t -> {
                            log.debug(String.format(
                                " @method [ Mono<TaskDTO> create(Mono<TaskDTO> dto) ] ->" +
                                " @body after saving [ task = %1$s ]", t));
                            return mapper.toDTO(t);
                        })
                    );
            });
    }

    @Override
    public Mono<TaskDTO> update(Mono<TaskDTO> dto) {
        return null;
    }

    @Override
    public Mono<TaskDTO> findOneById(Long idi) {
        return null;
    }

    @Override
    public Flux<TaskDTO> findAllByTasksContainer(Long idtc) {
        return repository.findAllByIdtc(idtc).map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long idi) {
        return null;
    }
}
