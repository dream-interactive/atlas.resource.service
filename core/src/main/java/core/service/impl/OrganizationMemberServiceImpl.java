package core.service.impl;

import api.dto.OrganizationMemberDTO;
import core.exception.CustomRequestException;
import core.mapper.OrganizationMemberMapper;
import core.repository.OrganizationMemberDAO;
import core.repository.OrganizationMemberRepository;
import core.repository.OrganizationRepository;
import core.service.OrganizationMemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizationMemberServiceImpl implements OrganizationMemberService {

    private final OrganizationMemberDAO dao;
    private final OrganizationMemberRepository repository;
    private final OrganizationMemberMapper mapper;

    private final OrganizationRepository organizationRepository;

    @Transactional
    public Mono<OrganizationMemberDTO> create(Mono<OrganizationMemberDTO> organizationMemberDTOMono) {
        return organizationMemberDTOMono
                .map(organizationMemberDTO -> {
                    log.debug(String.format(" @method [Mono<OrganizationMemberDTO> create (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @body: %s", organizationMemberDTO));
                    return mapper.toEntity(organizationMemberDTO);
                })
                .flatMap(organizationMember -> {
                    log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> create (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @body after @call mapper.toEntity(organizationMemberDTO): %s", organizationMember));
                    return organizationRepository.findById(organizationMember.getOrganizationId())
                            .hasElement()
                            .flatMap(organizationPresent -> {
                                if (!organizationPresent) {
                                    return Mono.error(
                                            new CustomRequestException(
                                                    String.format("ERROR ATLAS-7: Invalid organization id - %s", organizationMember.getOrganizationId()),
                                                    HttpStatus.BAD_REQUEST
                                            )
                                    );
                                }
                                log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> create (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @body after @call organizationRepository.findById(organizationMember.getOrganizationId()).hasElement(): %s", organizationMember.getOrganizationId()));
                                return repository
                                        .findAllByMemberIdAndOrganizationId(organizationMember.getMemberId(), organizationMember.getOrganizationId())
                                        .hasElements()
                                        .flatMap(isPresent -> {
                                            if (isPresent) {
                                                return Mono.error(
                                                        new CustomRequestException(
                                                                String.format("ERROR ATLAS-9: Member with id %s is already exists in organization %2$s", organizationMember.getMemberId(), organizationMember.getOrganizationId()),
                                                                HttpStatus.BAD_REQUEST
                                                        )
                                                );
                                            }
                                            log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> create (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @call dao.create(organizationMember): %s", organizationMember));
                                            return dao.create(organizationMember)
                                                    .then(repository.findByMemberIdAndOrganizationId(organizationMember.getMemberId(), organizationMember.getOrganizationId())
                                                            .map(saved -> {
                                                                log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> create (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] ->  @body after @call repository.findByMemberIdAndOrganizationId(organizationMember.getMemberId(), organizationMember.getOrganizationId(): %s", saved));
                                                                OrganizationMemberDTO organizationMemberDTO = mapper.toDTO(saved);
                                                                log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> create (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @body after @call mapper.toDTO(saved) : %s", organizationMemberDTO));
                                                                return organizationMemberDTO;
                                                            })
                                                    );
                                        });
                            });

                });
    }

    @Transactional
    public Mono<OrganizationMemberDTO> update(Mono<OrganizationMemberDTO> organizationMemberDTOMono) {
        return organizationMemberDTOMono
                .map(organizationMemberDTO -> {
                    log.debug(String.format(" @method [Mono<OrganizationMemberDTO> update (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @body: %s", organizationMemberDTO));
                    return mapper.toEntity(organizationMemberDTO);
                })
                .flatMap(organizationMember -> {
                    log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> update (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @body after @call mapper.toEntity(organizationMemberDTO): %s", organizationMember));
                    return repository
                            .findAllByOrganizationId(organizationMember.getOrganizationId())
                            .hasElements()
                            .flatMap(exist -> {
                                log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> update (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] ->  @body after @call repository..existsByOrganizationId(organizationMember.getOrganizationId()): %s", exist));
                                if (exist) {
                                    return repository
                                            .findAllByMemberIdAndOrganizationId(organizationMember.getMemberId(), organizationMember.getOrganizationId())
                                            .hasElements()
                                            .flatMap(isPresent -> {
                                                if (!isPresent) {
                                                    return Mono.error(
                                                            new CustomRequestException(
                                                                    String.format("ERROR ATLAS-9: Member with id %s is not exists in organization %2$s", organizationMember.getMemberId(), organizationMember.getOrganizationId()),
                                                                    HttpStatus.BAD_REQUEST
                                                            )
                                                    );
                                                }
                                                return dao
                                                        .update(organizationMember)
                                                        .then(repository.findByMemberIdAndOrganizationId(organizationMember.getMemberId(), organizationMember.getOrganizationId()))
                                                        .map(saved -> {
                                                            log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> update (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] ->  @body after @call repository.save(organizationMember: %s", saved));
                                                            OrganizationMemberDTO organizationMemberDTO = mapper.toDTO(saved);
                                                            log.debug(String.format(" @method [ Mono<OrganizationMemberDTO> update (Mono<OrganizationMemberDTO> organizationMemberDTOMono) ] -> @body after @call mapper.toDTO(saved) : %s", organizationMemberDTO));
                                                            return organizationMemberDTO;
                                                        });
                                            });
                                }
                                return Mono.error(
                                        new CustomRequestException(
                                                String.format("ERROR ATLAS-7: Invalid organization id - %s", organizationMember.getOrganizationId()),
                                                HttpStatus.BAD_REQUEST
                                        )
                                );
                            });

                });
    }

    public Flux<OrganizationMemberDTO> findByOrganizationId(UUID organizationId) {
        return repository
                .findAllByOrganizationId(organizationId)
                .map(found -> {
                    log.debug(String.format(" @method [ Flux<OrganizationMemberDTO> findByOrganizationId(UUID organizationId) ] -> @body after @call repository.findAllByOrganizationId(organizationId);  element: [ %s ]", found));
                    OrganizationMemberDTO organizationMemberDTO = mapper.toDTO(found);
                    log.debug(String.format(" @method [ Flux<OrganizationMemberDTO> findByOrganizationId(UUID organizationId) ] -> @body after @call mapper.toDTO(found);  element: [ %s ]", organizationMemberDTO));
                    return organizationMemberDTO;
                });
    }

    public Flux<OrganizationMemberDTO> findByMemberId(String memberId) {
        return repository
                .findAllByMemberId(memberId)
                .map(found -> {
                    log.debug(String.format(" @method [ Flux<OrganizationMemberDTO> findByMemberId(String memberId) ] -> @body after @call repository.findAllByMemberId(memberId);  element: [ %s ]", found));
                    OrganizationMemberDTO organizationMemberDTO = mapper.toDTO(found);
                    log.debug(String.format(" @method [ Flux<OrganizationMemberDTO> findByMemberId(String memberId) ] -> @body after @call mapper.toDTO(found);  element: [ %s ]", organizationMemberDTO));
                    return organizationMemberDTO;
                });
    }

    public Mono<Void> delete(String memberId, UUID organizationId) {
        return repository.deleteByMemberIdAndOrganizationId(memberId, organizationId);
    }
}
