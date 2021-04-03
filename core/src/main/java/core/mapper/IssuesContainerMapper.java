package core.mapper;

import api.dto.IssueDTO;
import api.dto.IssuesContainerDTO;
import core.entity.Issue;
import core.entity.IssuesContainer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class IssuesContainerMapper {

    @Mapping(source = "issues", target = "issues", qualifiedByName = "toIssues")
    public abstract IssuesContainer toEntity(IssuesContainerDTO dto,  @Context IssueMapper issueMapper);

    @Mapping(source = "issues", target = "issues", qualifiedByName = "toIssuesDTO")
    public abstract IssuesContainerDTO toDTO(IssuesContainer entity,  @Context IssueMapper issueMapper);

    @Named("toIssuesDTO")
    protected List<IssueDTO> toIssuesDTO(List<Issue> issues, @Context IssueMapper issueMapper) {
        return issues.stream().map(issueMapper::toDTO).collect(Collectors.toList());
    }

    @Named("toIssues")
    protected List<Issue> toIssues(List<IssueDTO> issues, @Context IssueMapper issueMapper) {
        return issues.stream().map(issueMapper::toEntity).collect(Collectors.toList());
    }
}
