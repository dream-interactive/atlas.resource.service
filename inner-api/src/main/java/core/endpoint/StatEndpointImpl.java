package core.endpoint;

import api.dto.Stat;
import api.endpoint.StatEndpoint;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 05.06.2021
 */

public class StatEndpointImpl implements StatEndpoint {
  @Override
  public Flux<Stat> findByProjectId(UUID id) {
    return null;
  }
}
