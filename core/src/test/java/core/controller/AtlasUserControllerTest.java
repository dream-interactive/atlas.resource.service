package core.controller;

import api.dto.AtlasUserAuthDTO;
import api.dto.ProjectDTO;
import core.entity.AtlasUser;
import core.mapper.AtlasUserAuthMapper;
import core.mapper.AtlasUserAuthMapperImpl;
import core.mapper.OrganizationMapperImpl;
import core.repository.AtlasUserDAO;
import core.repository.AtlasUserRepository;
import core.service.impl.AtlasUserServiceImpl;
import core.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import({AtlasUserServiceImpl.class, AtlasUserAuthMapperImpl.class})
@WebFluxTest(AtlasUserController.class)
class AtlasUserControllerTest {
    @MockBean
    private AtlasUserRepository repository;
    @MockBean
    private AtlasUserDAO dao;
    @Autowired
    private AtlasUserAuthMapper mapper;
    @Autowired
    private WebTestClient client;

    AtlasUserAuthDTO incomingUserDTO = new AtlasUserAuthDTO();


    @BeforeEach
    void init() {
        incomingUserDTO.setEmail("email@email.com");
        incomingUserDTO.setEmailVerified(true);
        incomingUserDTO.setName("name");
        incomingUserDTO.setNickname("nickname");
        incomingUserDTO.setPicture("pic");
        incomingUserDTO.setSub("github|4");
        incomingUserDTO.setUpdatedAt(null);
    }



    @Test
    @WithMockUser
    void testAtlasUserCreate() {
        AtlasUser atlasUser = mapper.toEntity(incomingUserDTO);

        when(dao.create(atlasUser))
                .thenReturn(Mono.just(1));
        when(repository.findById(atlasUser.getSub()))
                .thenReturn(Mono.just(atlasUser));

        client
                .mutateWith(csrf())
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingUserDTO), AtlasUserAuthDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AtlasUserAuthDTO.class)
                .isEqualTo(mapper.toDTO(atlasUser));
    }

    @Test
    @WithMockUser
    void update() {

        AtlasUser atlasUser = mapper.toEntity(incomingUserDTO);

        when(dao.update(atlasUser))
                .thenReturn(Mono.just(1));
        when(repository.findById(atlasUser.getSub()))
                .thenReturn(Mono.just(atlasUser));

        client
                .mutateWith(csrf())
                .put()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingUserDTO), AtlasUserAuthDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AtlasUserAuthDTO.class)
                .isEqualTo(mapper.toDTO(atlasUser));

    }

    @Test
    @WithMockUser
    void findById() {

        AtlasUser atlasUser = mapper.toEntity(incomingUserDTO);

        String id = "github|4";

        when(repository.findById(id))
                .thenReturn(Mono.just(atlasUser));
        client
                .mutateWith(csrf())
                .get()
                .uri("/api/users/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AtlasUserAuthDTO.class)
                .isEqualTo(mapper.toDTO(atlasUser));
    }
}