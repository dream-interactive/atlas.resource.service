package core.service.impl;

import api.dto.AtlasUserDTO;
import core.service.ProjectMembersService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 01.04.2021
 */
@Service
public class ProjectMembersServiceImpl implements ProjectMembersService {
    @Override
    public Flux<AtlasUserDTO> findAllByProjectId(UUID idp) {
        return null;
    }
}
