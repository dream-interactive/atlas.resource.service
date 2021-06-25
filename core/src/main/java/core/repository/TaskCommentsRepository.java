package core.repository;

import core.entity.TasksContainer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Created by Maksym Sevriukov.
 * Date: 19.03.2021
 */

public interface TaskCommentsRepository extends ReactiveCrudRepository<TasksContainer, Long> {
}
