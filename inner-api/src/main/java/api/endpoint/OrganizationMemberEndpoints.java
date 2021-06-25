package api.endpoint;

import api.dto.OrganizationMemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "api/organizations/members", produces = APPLICATION_JSON_VALUE)
public interface OrganizationMemberEndpoints {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Mono<OrganizationMemberDTO> create(@RequestBody Mono<OrganizationMemberDTO> dto);

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    Mono<OrganizationMemberDTO> update(@RequestBody Mono<OrganizationMemberDTO> dto);

    @GetMapping(params = "organizationId")
    @ResponseStatus(code = HttpStatus.OK)
    Flux<OrganizationMemberDTO> findByOrganizationId(@RequestParam(value = "organizationId") UUID organizationId);

    @GetMapping(params = "memberId")
    @ResponseStatus(code = HttpStatus.OK)
    Flux<OrganizationMemberDTO> findByMemberId(@RequestParam(value = "memberId") String memberId);

    @DeleteMapping(params = {"memberId", "organizationId"})
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    Mono<Void> delete(
            @RequestParam(value = "memberId") String memberId,
            @RequestParam(value = "organizationId") UUID organizationId
    );

}
