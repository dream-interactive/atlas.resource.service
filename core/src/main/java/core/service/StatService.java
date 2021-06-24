package core.service;

import api.dto.Stat;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 05.06.2021
 */

public interface StatService {
  Flux<Stat> findByProjectId(UUID idp);
}
