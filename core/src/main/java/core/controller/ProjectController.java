package core.controller;

import api.dto.ProjectDTO;
import core.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController()
@RequestMapping("api/")
public class ProjectController {

    private final ProjectService service;

    @PostMapping(value = "projects", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProjectDTO> save(@RequestBody Mono<ProjectDTO> projectDTOMono) {
        return service.save(projectDTOMono);
    }
}
