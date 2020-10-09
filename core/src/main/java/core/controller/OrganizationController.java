package core.controller;

import api.dto.OrganizationDTO;
import api.endpoint.OrganizationEndpoints;
import core.entity.Organization;
import core.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class OrganizationController implements OrganizationEndpoints {

    private final OrganizationService service;

    @Override
    public Mono<OrganizationDTO> create(@RequestBody Mono<OrganizationDTO> organizationDTOMono) {
        return service.save(organizationDTOMono);
    }

    @Override
    public Mono<OrganizationDTO> update(@RequestBody Mono<OrganizationDTO> organizationDTOMono) {
        return service.save(organizationDTOMono);
    }

    @Override
    public Mono<OrganizationDTO> findById(UUID organizationId) {
        return service.fetch(organizationId);
    }

    @Override
    public Mono<Void> delete(UUID organizationId) {
        return service.delete(organizationId);
    }

    @Override
    public Mono<Boolean> existByValidName(String validName) {
        return service.existByValidName(validName);
    }

    @Override
    public Flux<OrganizationDTO> findAllByUserId(String userId) {
        return service.findAllByUserId(userId);
    }
}
