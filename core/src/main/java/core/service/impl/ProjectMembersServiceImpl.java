package core.service.impl;

import api.dto.ProjectMemberDTO;
import core.entity.ProjectMember;
import core.mapper.ProjectMemberMapper;
import core.repository.ProjectMembersRepository;
import core.repository.ProjectRoleRepository;
import core.service.AtlasUserService;
import core.service.ProjectMembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

/** @author Maksym Sevriukov. Date: 01.04.2021 */
@Service
@RequiredArgsConstructor
public class ProjectMembersServiceImpl implements ProjectMembersService {

  private final ProjectMembersRepository repository;
  private final ProjectMemberMapper mapper;
  private final AtlasUserService atlasUserService;
  private final ProjectRoleRepository projectRoleRepository;

  @Override
  public Flux<ProjectMemberDTO> findAllByProjectId(UUID idp) {
    return repository
        .findAllByIdp(idp)
        .flatMap(projectMember -> mapper.toDTO(projectMember, atlasUserService, projectRoleRepository));
  }
}
