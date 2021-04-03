package core.service.impl;

import api.dto.IssueDTO;
import core.mapper.IssueMapper;
import core.repository.IssueRepository;
import core.service.IssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository repository;
    private final IssueMapper mapper;

    @Override
    public Mono<IssueDTO> create(Mono<IssueDTO> dto) {
        return null;
    }

    @Override
    public Mono<IssueDTO> update(Mono<IssueDTO> dto) {
        return null;
    }

    @Override
    public Mono<IssueDTO> findOneById(Long idi) {
        return null;
    }

    @Override
    public Flux<IssueDTO> findAllByIssuesContainer(Long idic) {
        return repository.findAllByIdic(idic).map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(Long idi) {
        return null;
    }
}
