package core.service;

import api.dto.ProjectMemberDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 01.04.2021
 */

public interface ProjectMembersService {
    Flux<ProjectMemberDTO> findAllByProjectId(UUID idp);

  Mono<ProjectMemberDTO> addMember(UUID idp, String email);
}
