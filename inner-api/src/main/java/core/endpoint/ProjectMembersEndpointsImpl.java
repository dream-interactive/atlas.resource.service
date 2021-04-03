package core.endpoint;

import api.dto.AtlasUserDTO;
import api.endpoint.ProjectMembersEndpoints;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 01.04.2021
 */

public class ProjectMembersEndpointsImpl implements ProjectMembersEndpoints {
    @Override
    public Flux<AtlasUserDTO> findAllByProjectId(UUID idp) {
        return null;
    }
}
