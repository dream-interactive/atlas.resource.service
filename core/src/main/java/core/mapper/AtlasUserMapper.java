package core.mapper;

import api.dto.AtlasUserDTO;
import core.entity.AtlasUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AtlasUserMapper {

    AtlasUserDTO toDTO (AtlasUser entity);

    AtlasUser toEntity (AtlasUserDTO dto);

}
