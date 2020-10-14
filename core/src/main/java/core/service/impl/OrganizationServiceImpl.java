package core.service.impl;

import api.dto.OrganizationDTO;
import core.entity.Organization;
import core.entity.OrganizationRoleMember;
import core.exception.CustomRequestException;
import core.mapper.OrganizationMapper;
import core.repository.OrganizationRepository;
import core.repository.OrganizationRoleMemberDAO;
import core.repository.OrganizationRoleMemberRepository;
import core.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.DatabaseClient;
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

    private final OrganizationRoleMemberDAO dao;

    private final OrganizationRepository repository;
    private final OrganizationRoleMemberRepository memberRepository;
    private final OrganizationMapper mapper;

    // save deprecated
    /*
    public Mono<OrganizationDTO> save(Mono<OrganizationDTO> organizationDTOMono) {
        return organizationDTOMono
                .map(organizationDTO -> {
                    log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] -> @body: " + organizationDTO);
                    return mapper.toEntity(organizationDTO);
                })
                .flatMap(organization -> {
                    log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toEntity(organizationDTO): " + organization);
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
                                log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] -> @call repository.save(organization)");
                                return repository.save(organization).flatMap(saved -> {
                                    log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] ->  @body after @call repository.save(organization): " + saved);
                                    Mono<Organization> findById = repository.findById(saved.getId());
                                    return findById.map(found -> {
                                        log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.findById(organization): " + found);
                                        OrganizationDTO organizationDTO = mapper.toDTO(found);
                                        log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toDTO(saved) : " + organizationDTO);
                                        return organizationDTO;
                                    });
                                });
                            });
                });
    }
    */

    @Transactional
    public Mono<OrganizationDTO> update(Mono<OrganizationDTO> organizationDTOMono) {
        return organizationDTOMono
                .map(organizationDTO -> {
                    log.debug(String.format(" @method [Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body: %s", organizationDTO));
                    return mapper.toEntity(organizationDTO);
                })
                .flatMap(organization -> {
                    log.debug(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toEntity(organizationDTO): " + organization);
                    if (organization.getId() == null) {
                        log.debug(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call if (organization.getId() == null): " + organization.getId());
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
                                    log.debug(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.findById(organization.getId()): " + result);
                                    if (result.getId() == null) {
                                        return Mono.error(
                                                new CustomRequestException(
                                                        String.format("ERROR ATLAS-7: Invalid organization id = %s", result.getId()),
                                                        HttpStatus.BAD_REQUEST)
                                        );
                                    }
                                    else if (!organization.getOwnerUserId().equals(result.getOwnerUserId())) { // if saving ownerId does not match ownerId in db
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
                                                    log.debug(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] ->  @body after @call repository.save(organization): " + saved);
                                                    Mono<Organization> findById = repository.findById(saved.getId());
                                                    return findById
                                                            .map(found -> {
                                                                log.debug(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.findById(organization): " + found);
                                                                OrganizationDTO organizationDTO = mapper.toDTO(found);
                                                                log.debug(" @method [ Mono<OrganizationDTO> update (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toDTO(saved) : " + organizationDTO);
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
                    log.debug(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toEntity(organizationDTO): " + organization);
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
                                log.debug(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @call repository.save(organization)");
                                return repository.save(organization).flatMap(saved -> {
                                    log.debug(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] ->  @body after @call repository.save(organization): " + saved);
                                    Mono<Organization> findById = repository.findById(saved.getId());
//                                    String sqlQuery = String.format("insert into org_role_member (organization_id, member_id, org_role_id) " +
//                                            "values ('%s', '%2$s', '%3$s') ", saved.getId(), saved.getOwnerUserId(), 1); // 1 - owner role
                                    OrganizationRoleMember organizationRoleMember = new OrganizationRoleMember(saved.getId(), saved.getOwnerUserId(), 1);
                                    log.debug(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] ->  @body before @call dao.create(organizationRoleMember): " + organizationRoleMember);
                                    return dao.create(organizationRoleMember)
                                            .then(findById)
                                            .map(found -> {
                                                log.debug(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.findById(organization): " + found);
                                                OrganizationDTO organizationDTO = mapper.toDTO(found);
                                                log.debug(" @method [ Mono<OrganizationDTO> create (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toDTO(saved) : " + organizationDTO);
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

    public Mono<Boolean> existByValidName(String validName) {
        return repository.findByValidName(validName).hasElement();
    }

    /* private methods */

//    private Mono<Void> executeSQL(String sql) {
//        return db.execute(sql).then();
//    }

}
