package core.service;

import api.dto.OrganizationDTO;
import core.entity.Organization;
import core.exception.CustomRequestException;
import core.mapper.OrganizationMapper;
import core.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository repository;
    private final OrganizationMapper mapper;

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
                                return repository.save(organization).map(saved -> {
                                    log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call repository.save(organization): " + saved);
                                    OrganizationDTO organizationDTO = mapper.toDTO(saved);
                                    log.debug(" @method [ Mono<OrganizationDTO> save (Mono<OrganizationDTO> organizationDTOMono) ] -> @body after @call mapper.toDTO(saved) : " + organizationDTO);
                                    return organizationDTO;
                                });
                            });
                });
    }

    public Mono<OrganizationDTO> fetch(UUID organizationId) {
        return repository.findById(organizationId)
                .flatMap(isPresent -> {
                    log.debug(" @method [ Mono<OrganizationDTO> fetch (UUID organizationId) ] -> @call repository.fetch(organizationId)");
                    if (isPresent == null) {
                        return Mono.error(
                                new CustomRequestException(
                                        "ERROR ATLAS-3: Couldn't find 'organization' with id - " + organizationId,
                                        HttpStatus.CONFLICT)
                        );
                    }

                    log.debug(" @method [ Mono<OrganizationDTO> fetch (UUID organizationId) ] -> @call repository.findById(organizationId)");
                    return repository.findById(organizationId).map(mapper::toDTO);

                });
    }

    public Mono<Void> delete(UUID organizationId) {
        return repository.deleteById(organizationId);
    }

    public Mono<Boolean> existByValidName(String validName) {

        return repository.findByValidName(validName).hasElement();

    }

    public Flux<OrganizationDTO> findAllByUserId(String userId) {

        return repository.findAllByOwnerUserId(userId).map(mapper::toDTO);

    }


}
