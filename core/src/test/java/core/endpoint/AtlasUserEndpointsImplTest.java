package core.endpoint;

import api.dto.AtlasUserAuthDTO;
import api.dto.AtlasUserDTO;
import core.entity.AtlasUser;
import core.exception.CustomExceptionHandler;
import core.mapper.AtlasUserAuthMapper;
import core.mapper.AtlasUserAuthMapperImpl;
import core.mapper.AtlasUserMapper;
import core.mapper.AtlasUserMapperImpl;
import core.dao.AtlasUserDAO;
import core.repository.AtlasUserRepository;
import core.security.Principal;
import core.service.impl.AtlasUserServiceImpl;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@Import({AtlasUserServiceImpl.class, AtlasUserAuthMapperImpl.class, AtlasUserMapperImpl.class})
@WebFluxTest(AtlasUserEndpointsImpl.class)
class AtlasUserEndpointsImplTest {
    @MockBean
    private AtlasUserRepository repository;
    @MockBean
    private AtlasUserDAO dao;
    @Autowired
    private AtlasUserMapper mapper;
    @Autowired
    private AtlasUserAuthMapper mapperAuth;
    @MockBean
    private Principal principal;
    @Autowired
    private WebTestClient client;

    AtlasUserAuthDTO incomingAuthUserDTO = new AtlasUserAuthDTO();
    AtlasUserDTO incomingUserDTO = new AtlasUserDTO();


    @BeforeEach
    void init() {
        incomingAuthUserDTO.setSub("00u1jfeoywzYV6c6V4x7");
        incomingAuthUserDTO.setEmail("email@email.com");
        incomingAuthUserDTO.setEmailVerified(true);
        incomingAuthUserDTO.setFamilyName("FamilyName");
        incomingAuthUserDTO.setGivenName("GivenName");
        incomingAuthUserDTO.setName("name");
        incomingAuthUserDTO.setLocal("local");

        incomingUserDTO.setSub("00u1jfeoywzYV6c6V4x7");
        incomingUserDTO.setEmail("email@email.com");
        incomingUserDTO.setEmailVerified(true);
        incomingUserDTO.setFamilyName("FamilyName");
        incomingUserDTO.setGivenName("GivenName");
        incomingUserDTO.setName("name");
        incomingUserDTO.setLocal("local");
        incomingUserDTO.setUserPicture("pic");
        incomingUserDTO.setLastModify(LocalDateTime.of(2020, 2, 3, 2,36, 23));
    }



    @Test
    @WithMockUser
    void create_correctAuthUserDTO_201atlasUserAuthDTO() {
        AtlasUser atlasUser = mapperAuth.toEntity(incomingAuthUserDTO);

        when(dao.create(atlasUser))
                .thenReturn(Mono.just(1));
        when(repository.findBySub(atlasUser.getSub()))
                .thenReturn(Mono.just(atlasUser));

        client
                .mutateWith(csrf())
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingAuthUserDTO), AtlasUserAuthDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AtlasUserAuthDTO.class)
                .isEqualTo(mapperAuth.toDTO(atlasUser));
    }

    @Test
    @WithMockUser
    void updateEmailVerification_correctAuthUserDTO_200AtlasUserAuthDTO() {
        AtlasUser atlasUser = mapperAuth.toEntity(incomingAuthUserDTO);

        when(principal.getUID())
                .thenReturn(Mono.just("00u1jfeoywzYV6c6V4x7"));
        when(dao.updateEmailVerification(atlasUser.getEmailVerified(), atlasUser.getSub()))
                .thenReturn(Mono.just(1));
        when(repository.findBySub(atlasUser.getSub()))
                .thenReturn(Mono.just(atlasUser));

        client
                .mutateWith(csrf())
                .put()
                .uri("/api/users/email_verification")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingAuthUserDTO), AtlasUserAuthDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AtlasUserAuthDTO.class)
                .isEqualTo(mapperAuth.toDTO(atlasUser));
    }

    @Test
    @WithMockUser
    void updateEmailVerification_UIDsDosntMatch_304CustomRequestException() {

        when(principal.getUID())
                .thenReturn(Mono.just("00u1jfeoywzYV6c6V4x8"));

        client
                .mutateWith(csrf())
                .put()
                .uri("/api/users/email_verification")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingAuthUserDTO), AtlasUserAuthDTO.class)
                .exchange()
                .expectStatus().isNotModified()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void update_correctAuthUserDTO_200MonoAtlasUserAuthDTO() {

        AtlasUser atlasUser = mapper.toEntity(incomingUserDTO);

        when(dao.update(atlasUser))
                .thenReturn(Mono.just(1));
        when(repository.findBySub(mapper.toEntity(incomingUserDTO).getSub()))
                .thenReturn(Mono.just(atlasUser));
        client
                .mutateWith(csrf())
                .put()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(incomingUserDTO), AtlasUserDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AtlasUserDTO.class)
                .isEqualTo(mapper.toDTO(atlasUser));
    }

    @Test
    @WithMockUser
    void findAtlasUserAuthById_subCorrect_200MonoAtlasUserAuthDTO() {
        AtlasUser atlasUser = mapperAuth.toEntity(incomingAuthUserDTO);

        when(repository.findBySub("00u1jfeoywzYV6c6V4x7"))
                .thenReturn(Mono.just(atlasUser));

        client
                .mutateWith(csrf())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/auth")
                        .queryParam("sub", "00u1jfeoywzYV6c6V4x7")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(AtlasUserAuthDTO.class)
                .isEqualTo(mapperAuth.toDTO(atlasUser));
    }

    @Test
    @WithMockUser
    void findAtlasUserAuthById_subIncorrect_404customRequestException() {
        when(repository.findBySub("Incorrect"))
                .thenReturn(Mono.empty());
        client
                .mutateWith(csrf())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/auth")
                        .queryParam("sub", "Incorrect")
                        .build()
                )
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void findAtlasUserAuthById_subEmpty_400customRequestException() {
        client
                .mutateWith(csrf())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/auth")
                        .queryParam("sub", "")
                        .build()
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void findAtlasUserById_subCorrect_200MonoAtlasUserAuthDTO() {
        AtlasUser atlasUser = mapperAuth.toEntity(incomingAuthUserDTO);

        when(repository.findBySub("00u1jfeoywzYV6c6V4x7"))
                .thenReturn(Mono.just(atlasUser));

        client
                .mutateWith(csrf())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/auth")
                        .queryParam("sub", "00u1jfeoywzYV6c6V4x7")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(AtlasUserDTO.class)
                .isEqualTo(mapper.toDTO(atlasUser));
    }

    @Test
    @WithMockUser
    void findAtlasUserById_subIncorrect_404customRequestException() {
        when(repository.findBySub("Incorrect"))
                .thenReturn(Mono.empty());
        client
                .mutateWith(csrf())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/Incorrect")
                        .build()
                )
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(CustomExceptionHandler.class);
    }

    @Test
    @WithMockUser
    void findAtlasUserById_subEmpty_400customRequestException() {
        client
                .mutateWith(csrf())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/       ")
                        .build()
                )
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(CustomExceptionHandler.class);
    }
}