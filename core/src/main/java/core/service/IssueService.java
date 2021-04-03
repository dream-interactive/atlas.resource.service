package core.service;

import api.dto.IssueDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IssueService {
    Mono<IssueDTO> create(Mono<IssueDTO> dto);

    Mono<IssueDTO> update(Mono<IssueDTO> dto);

    Mono<IssueDTO> findOneById(Long idi);

    Flux<IssueDTO> findAllByIssuesContainer(Long idic);

    Mono<Void> delete(Long idi);
}
