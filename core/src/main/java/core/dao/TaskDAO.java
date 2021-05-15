package core.dao;

import reactor.core.publisher.Flux;
import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 14.04.2021
 */

public interface TaskDAO {
  Flux<String> findLabelsByIdp(UUID idp);
}
