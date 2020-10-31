package core.mapper;

import api.dto.AtlasUserAuthDTO;
import core.entity.AtlasUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AtlasUserAuthMapper {

    AtlasUserAuthDTO toDTO (AtlasUser entity);

    AtlasUser toEntity (AtlasUserAuthDTO dto);

}
