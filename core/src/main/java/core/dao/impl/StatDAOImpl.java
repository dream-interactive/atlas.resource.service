package core.dao.impl;

import api.dto.Stat;
import core.dao.StatDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 05.06.2021
 */
@Repository
@RequiredArgsConstructor
public class StatDAOImpl implements StatDAO {
  private final DatabaseClient client;

  @Override
  public Flux<Stat> findByProjectId(UUID idp) {
    return client
        .sql("select * from task_history where task_history.row_history -> 'project_id' ? :idp")
        .bind("idp", idp)
        .fetch()
        .all()
        .cast(Stat.class);
  }
}
