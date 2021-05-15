package core.endpoint;

import api.dto.AtlasUserDTO;
import api.dto.ProjectMemberDTO;
import api.endpoint.ProjectMembersEndpoints;
import core.service.ProjectMembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @author Maksym Sevriukov
 * Date: 01.04.2021
 * */
@RestController
@RequiredArgsConstructor
public class ProjectMembersEndpointsImpl implements ProjectMembersEndpoints {
  private final ProjectMembersService service;

  @Override
  public Flux<ProjectMemberDTO> findAllByProjectId(UUID idp) {
    return service.findAllByProjectId(idp);
  }
}
