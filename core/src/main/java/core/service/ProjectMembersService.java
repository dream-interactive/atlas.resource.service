package core.service;

import api.dto.AtlasUserDTO;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 01.04.2021
 */

public interface ProjectMembersService {
    Flux<AtlasUserDTO> findAllByProjectId(UUID idp);

}
