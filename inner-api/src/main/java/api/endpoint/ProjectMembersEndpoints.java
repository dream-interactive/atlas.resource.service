package api.endpoint;

import api.dto.AtlasUserDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

/**
 *
 * @author Maksym Sevriukov
 * Date: 01.04.2021
 */
@RequestMapping(value = "api/projects", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProjectMembersEndpoints {

    @GetMapping("/{idp}/members")
    @ResponseStatus(OK)
    Flux<AtlasUserDTO> findAllByProjectId(@PathVariable("idp") UUID idp);
}
