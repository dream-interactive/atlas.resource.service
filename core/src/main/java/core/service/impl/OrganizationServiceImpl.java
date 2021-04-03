package core.service.impl;

import api.dto.OrganizationDTO;
import core.entity.Organization;
import core.entity.OrganizationMember;
import core.exception.CustomRequestException;
import core.mapper.OrganizationMapper;
import core.repository.OrganizationRepository;
import core.dao.OrganizationMemberDAO;
import core.repository.OrganizationMemberRepository;
import core.service.OrganizationService;
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
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationMemberDAO dao;

    private final OrganizationRepository repository;
    private final OrganizationMemberRepository memberRepository;
    private final OrganizationMapper mapper;

    @Transactional
    public Mono<OrganizationDTO> update(Mono<OrganizationDTO> organizationDTOMono) {
        return organizationDTOMono
                .map(organizationDTO -> {
                    log.debug(String.format(" @method [Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body: %s", organizationDTO));
                    return mapper.toEntity(organizationDTO);
                })
                .flatMap(organization -> {
                    log.debug(String.format(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toEntity(organizationDTO): %s", organization));
                    if (organization.getId() == null) {
                        log.debug(String.format(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call if (organization.getId() == null): %s", organization.getId()));
                        return Mono.error(
                                new CustomRequestException(
                                        String.format("ERROR ATLAS-7: Invalid organization id = %s", organization.getId()),
                                        HttpStatus.BAD_REQUEST)
                        );
                    }
                    else {
                        return repository
                                .findById(organization.getId())
                                .flatMap(result -> {
                                    log.debug(String.format(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.findById(organization.getId()): %s", result));
                                    if (!organization.getOwnerUserId().equals(result.getOwnerUserId())) { // if saving ownerId does not match ownerId in db
                                        return Mono.error(
                                                new CustomRequestException(
                                                        String.format("ERROR ATLAS-8: Owner ids does not match: expected %s, was %2$s", result.getOwnerUserId(), organization.getOwnerUserId()),
                                                        HttpStatus.CONFLICT)
                                        );
                                    }
                                    else {
                                        return repository
                                                .save(organization)
                                                .flatMap(saved -> {
                                                    log.debug(String.format(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] ->  @body after @call repository.save(organization): %s", saved));
                                                    Mono<Organization> findById = repository.findById(saved.getId());
                                                    return findById
                                                            .map(found -> {
                                                                log.debug(String.format(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.findById(organization): %s", found));
                                                                OrganizationDTO organizationDTO = mapper.toDTO(found);
                                                                log.debug(String.format(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toDTO(saved) : %s", organizationDTO));
                                                                return organizationDTO;
                                                            });
                                                });
                                    }
                                })
                                .switchIfEmpty(
                                        Mono.error(new CustomRequestException(
                                                String.format("ERROR ATLAS-3: Could not find organization with id - %s", organization.getId()),
                                                HttpStatus.BAD_REQUEST)
                                        )
                                );
                    }
                });
    }

    @Transactional
    public Mono<OrganizationDTO> create(Mono<OrganizationDTO> organizationDTOMono) {
        return organizationDTOMono
                .map(organizationDTO -> {
                    log.debug(String.format(" @method [Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @body: %s", organizationDTO));
                    return mapper.toEntity(organizationDTO);
                })
                .flatMap(organization -> {
                    log.debug(String.format(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toEntity(organizationDTO): %s", organization));
                    return repository
                            .findByValidName(organization.getValidName())
                            .hasElement()
                            .flatMap(isPresent -> {
                                if (isPresent && organization.getId() == null) {
                                    return Mono.error(
                                            new CustomRequestException(
                                                    "ERROR ATLAS-2: Organization with this name already exists.",
                                                    HttpStatus.CONFLICT)
                                    );
                                }
                                log.debug(String.format(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @call repository.save(organization): %s", isPresent));
                                return repository.save(organization).flatMap(saved -> {
                                    log.debug(String.format(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] ->  @body after @call repository.save(organization): %s", saved));
                                    Mono<Organization> findById = repository.findById(saved.getId());
                                    OrganizationMember organizationMember = new OrganizationMember(saved.getId(), saved.getOwnerUserId(), 1);
                                    log.debug(String.format(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] ->  @body before @call dao.create(organizationRoleMember): %s", organizationMember));
                                    return dao.create(organizationMember)
                                            .then(findById)
                                            .map(found -> {
                                                log.debug(String.format(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.findById(organization): %s", found));
                                                OrganizationDTO organizationDTO = mapper.toDTO(found);
                                                log.debug(String.format(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toDTO(saved) : %s", organizationDTO));
                                                return organizationDTO;
                                            });
                                });
                            });
                });
    }

    public Mono<OrganizationDTO> fetch(UUID organizationId) {
        return repository.findById(organizationId)
                .map(organization -> {
                    log.debug(String.format(" @method [ Mono<OrganizationDTO> fetch (UUID organizationId) ] -> @body after repository.findById(organizationId): %s", organization));
                    return mapper.toDTO(organization);
                })
                .switchIfEmpty(
                        Mono.error(new CustomRequestException(
                                String.format("ERROR ATLAS-3: Could not find organization with id - %s", organizationId),
                                HttpStatus.CONFLICT)
                        )
                );
    }

    public Mono<Void> delete(UUID organizationId) {
        return repository.deleteById(organizationId);
    }

    public Flux<OrganizationDTO> findByUserId(String userId) {
        log.debug(String.format(" @method [ Flux<OrganizationDTO> findAllByUserId(String userId) ] -> @param: %s", userId));
        return memberRepository
                .findAllByMemberId(userId)
                .flatMap(orm -> {
                    log.debug(String.format(" @method [ Flux<OrganizationDTO> findAllByUserId(String userId) ] -> @call flatMap for each element after @call memberRepository.findAllByMemberId(userId);  element: [ %s ]", orm));
                    return repository.findById(orm.getOrganizationId()).map(organization -> {
                        log.debug(String.format("  @method [ Flux<OrganizationDTO> findAllByUserId(String userId) ] -> @body after @call repository.findById(orm.getOrganizationId()) for each element %s", organization));
                        OrganizationDTO organizationDTO = mapper.toDTO(organization);
                        log.debug(String.format("  @method [ Flux<OrganizationDTO> findAllByUserId(String userId) ] -> @body after @call mapper.toDTO(organization) for each element %s", organizationDTO));
                        return organizationDTO;
                    });
                });
    }

    @Override
    public Mono<OrganizationDTO> findByValidName(String validName) {
        log.debug(String.format(" @method [ Mono<OrganizationDTO> findByValidName(String validName) ] -> @param: %s", validName));
        return repository.findByValidName(validName).map(mapper::toDTO);
    }

}
