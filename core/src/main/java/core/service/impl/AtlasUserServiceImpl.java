package core.service.impl;

import api.dto.AtlasUserAuthDTO;
import core.exception.CustomRequestException;
import core.mapper.AtlasUserAuthMapper;
import core.repository.AtlasUserDAO;
import core.repository.AtlasUserRepository;
import core.service.AtlasUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
@Slf4j
public class AtlasUserServiceImpl implements AtlasUserService {

    private final AtlasUserRepository repository;
    private final AtlasUserAuthMapper mapper;
    private final AtlasUserDAO dao;

    @Override
    public Mono<AtlasUserAuthDTO> findById(String sub) {
        return repository
                .findById(sub)
                .map(mapper::toDTO)
                .switchIfEmpty(
                        Mono.error(new CustomRequestException(
                                String.format("ERROR ATLAS-12: Could not find user with sub_id - %s", sub),
                                HttpStatus.NOT_FOUND)
                        )
                );
    }

    @Override
    public Mono<AtlasUserAuthDTO> create(Mono<AtlasUserAuthDTO> userDTOMono) {
        return userDTOMono
                .map(dto -> {
                    log.debug(String.format(" @method [ Mono<AtlasUserAuthDTO> create (Mono<AtlasUserAuthDTO> userDTOMono) ] -> @body  %s", dto));
                    return mapper.toEntity(dto);
                })
                .flatMap(user -> dao.create(user)
                        .then(repository.findById(user.getSub()))
                        .map(found -> {
                            log.debug(String.format(" @method [ Mono<AtlasUserAuthDTO> create (Mono<AtlasUserAuthDTO> userDTOMono) ] -> @body  after dao.create(user) %s", found));
                            return mapper.toDTO(found);
                        }));
    }

    @Override
    public Mono<AtlasUserAuthDTO> update(Mono<AtlasUserAuthDTO> userDTOMono) {
        return userDTOMono
                .map(dto -> {
                    log.debug(String.format(" @method [ Mono<AtlasUserAuthDTO> update (Mono<AtlasUserAuthDTO> userDTOMono) ] -> @body  %s", dto));
                    return mapper.toEntity(dto);
                })
                .flatMap(user -> dao.update(user)
                        .then(repository.findById(user.getSub()))
                        .map( found -> {
                        log.debug(String.format(" @method [ Mono<AtlasUserAuthDTO> update (Mono<AtlasUserAuthDTO> userDTOMono) ] -> @body  after dao.update(user) %s", found));
                        return mapper.toDTO(found);
                }));
    }
}
