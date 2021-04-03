package core.mapper;

import api.dto.IssueDTO;
import core.entity.Issue;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IssueMapper {

    Issue toEntity(IssueDTO dto);
    IssueDTO toDTO(Issue entity);
}
