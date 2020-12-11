package core.service.impl;

import api.dto.AtlasExceptionDTO;
import core.mapper.AtlasExceptionsMapper;
import core.repository.AtlasExceptionRepository;
import core.service.AtlasExceptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AtlasExceptionsServiceImpl implements AtlasExceptionsService {

    private final AtlasExceptionRepository repository;
    private final AtlasExceptionsMapper mapper;

    @Override
    public Flux<AtlasExceptionDTO> findAll() {
        return repository.findAll().map(mapper::toDTO);
    }

    @Override
    public Flux<AtlasExceptionDTO> findAllPublic() {
        return repository.findAllByPrivateExceptionFalse().map(mapper::toDTO);
    }
}
