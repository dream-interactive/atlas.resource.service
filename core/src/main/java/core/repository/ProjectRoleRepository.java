package core.repository;

import core.entity.ProjectRole;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Maksym Sevriukov.
 * Date: 15.04.2021
 */
@Repository
public interface ProjectRoleRepository extends R2dbcRepository<ProjectRole, Integer> {
}
