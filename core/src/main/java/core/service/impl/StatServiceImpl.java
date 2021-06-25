package core.service.impl;

import api.dto.Stat;
import core.dao.StatDAO;
import core.entity.Task;
import core.repository.TaskRepository;
import core.repository.TasksContainerRepository;
import core.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 05.06.2021
 */
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

  private final StatDAO dao;
  private final TaskRepository taskRepository;
  private final TasksContainerRepository tasksContainerRepository;

  @Override
  public Flux<Stat> findByProjectId(UUID idp) {

    List<Task> tasks = new ArrayList<>();

     tasksContainerRepository.findAllByIdp(idp)
        .flatMap(tasksContainer -> {
          Long idtc = tasksContainer.getIdtc();
          return taskRepository.findAllByIdtc(idtc);
        });
    return null;
  }
}
