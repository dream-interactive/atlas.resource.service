package core.repository;

import core.entity.IssuesContainer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Created by Maksym Sevriukov.
 * Date: 19.03.2021
 */

public interface IssueCommentsRepository extends ReactiveCrudRepository<IssuesContainer, Long> {
}
