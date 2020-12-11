package core.service.impl;

import api.dto.AtlasUserAuthDTO;
import api.dto.AtlasUserDTO;
import core.exception.CustomRequestException;
import core.mapper.AtlasUserAuthMapper;
import core.mapper.AtlasUserMapper;
import core.repository.AtlasUserDAO;
import core.repository.AtlasUserRepository;
import core.security.Principal;
import core.service.AtlasUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class AtlasUserServiceImpl implements AtlasUserService {

    private final AtlasUserRepository repository;
    private final AtlasUserAuthMapper mapperAuth;
    private final AtlasUserMapper mapper;
    private final AtlasUserDAO dao;
    private final Principal principal;

    @Override
    public Mono<AtlasUserAuthDTO> updateEmailVerification(Mono<AtlasUserAuthDTO> dto) {
        return dto.flatMap(atlasUserAuthDTO -> {
            log.debug(String.format(" @method [ Mono<AtlasUserAuthDTO> updateEmailVerification(Mono<AtlasUserAuthDTO> dto ] -> @body  %s", atlasUserAuthDTO));
            return principal.getUID().flatMap(uid -> { // get id from principal

                if (uid.matches(atlasUserAuthDTO.getSub())) { // simple verification
                    return dao.updateEmailVerification(atlasUserAuthDTO.getEmailVerified(), atlasUserAuthDTO.getSub())
                            .then(repository.findBySub(atlasUserAuthDTO.getSub()))
                            .map( found -> {
                                log.debug(String.format(
                                                " @method [ Mono<AtlasUserAuthDTO> updateEmailVerification (Mono<AtlasUserAuthDTO> userDTOMono) ] ->" +
                                                " @body  after dao.updateEmailVerification(user) %s", found));
                                return mapperAuth.toDTO(found);
                            });
                } else {
                    log.error(String.format(
                        " @method [ Mono<AtlasUserAuthDTO> updateEmailVerification(Mono<AtlasUserAuthDTO> dto ] -> " +
                        "ERROR ATLAS-102: user ids does not match. "                                                 +
                        "[ Principal: %1$s ], "                                                                      +
                        "[ atlasUserAuthDTO.getSub(): %2$s ] "                                                       ,
                        uid,
                        atlasUserAuthDTO.getSub()));
                    return Mono.error(
                            new CustomRequestException(
                                    "ERROR ATLAS-102",
                                    HttpStatus.NOT_MODIFIED)
                    );
                }
            });
        });
    }

    @Override
    public Mono<AtlasUserAuthDTO> findAtlasUserAuthById(String sub) {
        if (sub.trim().isEmpty()) {
            return Mono.error(
                    new CustomRequestException(
                            "ERROR ATLAS-12: User id can't be null or empty!",
                            HttpStatus.BAD_REQUEST)
            );
        }
        return repository
                .findBySub(sub.trim())
                .map(mapperAuth::toDTO)
                .switchIfEmpty(
                        Mono.error(new CustomRequestException(
                                String.format("ERROR ATLAS-12: Could not find user with sub - %s", sub),
                                HttpStatus.NOT_FOUND)
                        )
                );
    }
    @Override
    public Mono<AtlasUserDTO> findAtlasUserById(String sub) {
        if (sub.trim().isEmpty()) {
            return Mono.error(
                    new CustomRequestException(
                            "ERROR ATLAS-12: User id can't be empty!",
                            HttpStatus.BAD_REQUEST)
            );
        }
        return repository
                .findBySub(sub)
                .map(mapper::toDTO)
                .switchIfEmpty(
                        Mono.error(new CustomRequestException(
                                String.format("ERROR ATLAS-12: Could not find user with sub - %s", sub),
                                HttpStatus.NOT_FOUND)
                        )
                );
    }

    @Override
    public Mono<AtlasUserAuthDTO> create(Mono<AtlasUserAuthDTO> userDTOMono) {
        return userDTOMono
                .map(dto -> {
                    log.debug(String.format(" @method [ Mono<AtlasUserAuthDTO> create (Mono<AtlasUserAuthDTO> userDTOMono) ] -> @body  %s", dto));
                    return mapperAuth.toEntity(dto);
                })
                .flatMap(user -> dao.create(user)
                        .then(repository.findBySub(user.getSub()))
                        .map(found -> {
                            log.debug(String.format(" @method [ Mono<AtlasUserAuthDTO> create (Mono<AtlasUserAuthDTO> userDTOMono) ] -> @body  after dao.create(user) %s", found));
                            return mapperAuth.toDTO(found);
                        }));
    }





    @Override
    public Mono<AtlasUserDTO> update(Mono<AtlasUserDTO> atlasUserDTOMono) {
        return atlasUserDTOMono
                .map(dto -> {
                    log.debug(String.format(" @method [ Mono<AtlasUserDTO> update(Mono<AtlasUserDTO> user) ] -> @body  %s", dto));
                    return mapper.toEntity(dto);
                })
                .flatMap(user -> dao.update(user)
                        .then(repository.findBySub(user.getSub()))
                        .map(found -> {
                            log.debug(String.format(" @method [ Mono<AtlasUserDTO> update(Mono<AtlasUserDTO> user) ] -> @body  after dao.update(user) %s", found));
                            return mapper.toDTO(found);
                        }));
    }


}
