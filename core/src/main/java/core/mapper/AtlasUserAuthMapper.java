package core.mapper;

import api.dto.AtlasUserAuthDTO;
import core.entity.AtlasUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AtlasUserAuthMapper {


    AtlasUserAuthDTO toDTO (AtlasUser entity);

    @Mapping(target = "lastModify", ignore = true)
    @Mapping(target = "userPicture", ignore = true)
    AtlasUser toEntity (AtlasUserAuthDTO dto);

}
