package core.service.impl;

import api.dto.IssueDTO;
import api.dto.IssuesContainerDTO;
import core.entity.AtlasUser;
import core.entity.Issue;
import core.entity.IssuesContainer;
import core.mapper.IssueMapper;
import core.mapper.IssuesContainerMapper;
import core.repository.IssueRepository;
import core.repository.IssuesContainerRepository;
import core.security.Principal;
import core.service.IssuesContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IssuesContainerServiceImpl implements IssuesContainerService {

    private final IssuesContainerRepository repository;
    private final IssuesContainerMapper mapper;
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;
    private final Principal principal;

    @Override
    public Mono<IssuesContainerDTO> create(Mono<IssuesContainerDTO> monoDTO) {

        return principal.getUID()
                .flatMap(uid -> {
                    log.debug(String.format(
                            " @method [ Mono<IssuesContainerDTO> create(Mono<IssuesContainerDTO> monoDTO) ] ->" +
                            " @user [ sub = %1$s ]", uid));
                    return monoDTO; })
                .map(dto -> {
                    log.debug(String.format(
                            " @method [ Mono<IssuesContainerDTO> create(Mono<IssuesContainerDTO> monoDTO) ] ->" +
                            " @body [ dto = %1$s ]", dto));
                    IssuesContainer issuesContainer = mapper.toEntity(dto, issueMapper);
                    log.debug(String.format(
                            " @method [ Mono<IssuesContainerDTO> create(Mono<IssuesContainerDTO> monoDTO) ] ->" +
                            " @body after mapper [ issuesContainer = %1$s ]", issuesContainer));
                    return issuesContainer; })
                .flatMap(ic -> repository.save(ic).map(issuesContainer -> {
                    log.debug(String.format(
                            " @method [ Mono<IssuesContainerDTO> create(Mono<IssuesContainerDTO> monoDTO) ] ->" +
                            " @body after saving [ issuesContainer = %1$s ]", issuesContainer));
                    return mapper.toDTO(issuesContainer, issueMapper);
                }));
    }

    @Override
    public Mono<IssuesContainerDTO> update(Mono<IssuesContainerDTO> dto) {
        return null;
    }

    @Override
    public Mono<IssuesContainerDTO> findOneById(Long idic) {
        return null;
    }

    @Override
    public Flux<IssuesContainerDTO> findAllByProjectId(UUID idp) {
        return repository
                .findAllByIdp(idp)
                .sort(Comparator.comparingInt(IssuesContainer::getIndexNumber))
                .concatMap(container -> {
                    return issueRepository.findAllByIdic(container.getIdic())
                            .sort(Comparator.comparingInt(Issue::getIndexNumber))
                            .collectList()
                            .map(is -> {
                                container.setIssues(is);
                                return container;
                            }).map(c -> mapper.toDTO(c, issueMapper))
                            .flux();
                });
    }

    @Override
    public Mono<Void> delete(Long idic) {
        return null;
    }
}
