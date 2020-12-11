package core.mapper;

import api.dto.AtlasExceptionDTO;
import core.entity.AtlasException;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AtlasExceptionsMapper {

    AtlasExceptionDTO toDTO (AtlasException entity);
    AtlasException toEntity (AtlasExceptionDTO dto);
}
