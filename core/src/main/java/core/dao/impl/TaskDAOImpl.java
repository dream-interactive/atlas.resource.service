package core.dao.impl;

import core.dao.TaskDAO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static reactor.core.publisher.Flux.just;

/** @author Maksym Sevriukov. Date: 14.04.2021 */
@Repository
@RequiredArgsConstructor
public class TaskDAOImpl implements TaskDAO {

  private final DatabaseClient client;

  @Override
  public Flux<String> findLabelsByIdp(UUID idp) {
    return client
        .sql("select labels from task where project_id = :idp")
        .bind("idp", idp)
        .fetch()
        .all()
        .map(
            m -> {
              String[] labels = (String[]) m.get("labels");
              return Arrays.stream(labels).distinct().toArray(String[]::new);

            })
        .flatMap(Flux::just);
  }
}
