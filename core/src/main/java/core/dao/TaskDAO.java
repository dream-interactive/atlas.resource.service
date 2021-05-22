package core.dao;

import core.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 14.04.2021
 */

public interface TaskDAO {
  Flux<String> findLabelsByIdp(UUID idp);
}
