package core.endpoint;

import api.dto.OrganizationMemberDTO;
import api.endpoint.OrganizationMemberEndpoints;
import core.service.OrganizationMemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class OrganizationMemberEndpointsImpl implements OrganizationMemberEndpoints {

    private final OrganizationMemberService service;

    @Override
    public Mono<OrganizationMemberDTO> create(Mono<OrganizationMemberDTO> dto) {
        return service.create(dto);
    }

    @Override
    public Mono<OrganizationMemberDTO> update(Mono<OrganizationMemberDTO> dto) {
        return service.update(dto);
    }

    @Override
    public Flux<OrganizationMemberDTO> findByOrganizationId(UUID organizationId) {
        return service.findByOrganizationId(organizationId);
    }

    @Override
    public Flux<OrganizationMemberDTO> findByMemberId(String memberId) {
        return service.findByMemberId(memberId);
    }

    @Override
    public Mono<Void> delete(String memberId, UUID organizationId) {
        return service.delete(memberId, organizationId);
    }
}
