package core.endpoint;

import api.dto.IssuesContainerDTO;
import api.endpoint.IssuesContainerEndpoints;
import core.service.IssuesContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IssuesContainerEndpointsImpl implements IssuesContainerEndpoints {

    private final IssuesContainerService service;

    @Override
    public Mono<IssuesContainerDTO> create(Mono<IssuesContainerDTO> dto) {
        return null;
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
        return service.findAllByProjectId(idp);
    }

    @Override
    public Mono<Void> delete(Long idic) {
        return null;
    }
}
