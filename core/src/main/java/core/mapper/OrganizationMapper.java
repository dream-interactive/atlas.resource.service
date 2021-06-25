package core.mapper;

import api.dto.OrganizationDTO;
import core.entity.Organization;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrganizationMapper {

  OrganizationDTO toDTO(Organization entity);

  @Mapping(target = "lastModify", ignore = true)
  Organization toEntity(OrganizationDTO dto);
}
