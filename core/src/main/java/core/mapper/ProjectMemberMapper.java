package core.mapper;

import api.dto.ProjectMemberDTO;
import core.entity.ProjectMember;
import core.repository.ProjectRoleRepository;
import core.service.AtlasUserService;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;

/**
 * @author Maksym Sevriukov.
 * Date: 15.04.2021
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ProjectMemberMapper {


  public Mono<ProjectMemberDTO> toDTO(ProjectMember entity,
                                      @Context AtlasUserService atlasUserService,
                                      @Context ProjectRoleRepository prRepo) {

    ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO();

    if (entity == null) {
      return Mono.empty();
    } else {
      return atlasUserService
          .findAtlasUserById(entity.getSub())
          .flatMap(
              atlasUserDTO -> {
                projectMemberDTO.setIdp(entity.getIdp());
                projectMemberDTO.setUser(atlasUserDTO);
                return prRepo.findById(entity.getRoleId());
              })
          .flatMap(
              role -> {
                projectMemberDTO.setRole(role.getRole());
                return Mono.just(projectMemberDTO);
              });
    }
  }
}
