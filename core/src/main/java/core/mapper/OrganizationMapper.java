package core.mapper;

import api.dto.OrganizationDTO;
import core.entity.Organization;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class OrganizationMapper {

    public abstract OrganizationDTO toDTO (Organization entity);

    @Mapping(target = "lastModify", ignore = true)
    public abstract Organization toEntity (OrganizationDTO dto);

}
