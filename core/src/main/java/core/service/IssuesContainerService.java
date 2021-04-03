package core.service;

import api.dto.IssuesContainerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IssuesContainerService {

    Mono<IssuesContainerDTO> create(Mono<IssuesContainerDTO> dto);

    Mono<IssuesContainerDTO> update(Mono<IssuesContainerDTO> dto);

    Mono<IssuesContainerDTO> findOneById(Long idic);

    Flux<IssuesContainerDTO> findAllByProjectId(UUID idp);

    Mono<Void> delete(Long idic);
}
