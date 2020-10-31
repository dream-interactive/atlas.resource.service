package core.mapper;

import api.dto.AtlasUserAuthDTO;
import core.entity.AtlasUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Import(AtlasUserAuthMapperImpl.class)
@WebFluxTest(AtlasUserAuthMapper.class)
class AtlasUserAuthMapperTest {

    @Autowired
    private AtlasUserAuthMapper mapper;

    @Test
    void testToDTOAtlasUserAuthMapper_returnDto() {
        AtlasUserAuthDTO atlasUserAuthDTO = new AtlasUserAuthDTO();
        atlasUserAuthDTO.setSub("github|4");
        atlasUserAuthDTO.setNickname( "Jorik" );
        atlasUserAuthDTO.setName( "Jorik" );
        atlasUserAuthDTO.setPicture( "pic" );
        atlasUserAuthDTO.setEmail( "email@email.com" );
        atlasUserAuthDTO.setEmailVerified( true );
        atlasUserAuthDTO.setUpdatedAt( ZonedDateTime.of(LocalDateTime.of(2020, 12,12, 12, 12), ZoneId.of("Europe/Paris")));

        AtlasUser userEntity = new AtlasUser();

        userEntity.setSub("github|4");
        userEntity.setNickname( "Jorik" );
        userEntity.setName( "Jorik" );
        userEntity.setPicture( "pic" );
        userEntity.setEmail( "email@email.com" );
        userEntity.setEmailVerified( true );
        userEntity.setUpdatedAt( ZonedDateTime.of(LocalDateTime.of(2020, 12,12, 12, 12), ZoneId.of("Europe/Paris")));

        Assertions.assertEquals(atlasUserAuthDTO, mapper.toDTO(userEntity));

    }

    @Test
    void testToEntityAtlasUserAuthMapper_returnEntity() {
        AtlasUserAuthDTO atlasUserAuthDTO = new AtlasUserAuthDTO();
        atlasUserAuthDTO.setSub("github|4");
        atlasUserAuthDTO.setNickname( "Jorik" );
        atlasUserAuthDTO.setName( "Jorik" );
        atlasUserAuthDTO.setPicture( "pic" );
        atlasUserAuthDTO.setEmail( "email@email.com" );
        atlasUserAuthDTO.setEmailVerified( true );
        atlasUserAuthDTO.setUpdatedAt( ZonedDateTime.of(LocalDateTime.of(2020, 12,12, 12, 12), ZoneId.of("Europe/Paris")));

        AtlasUser userEntity = new AtlasUser();

        userEntity.setSub("github|4");
        userEntity.setNickname( "Jorik" );
        userEntity.setName( "Jorik" );
        userEntity.setPicture( "pic" );
        userEntity.setEmail( "email@email.com" );
        userEntity.setEmailVerified( true );
        userEntity.setUpdatedAt( ZonedDateTime.of(LocalDateTime.of(2020, 12,12, 12, 12), ZoneId.of("Europe/Paris")));

        Assertions.assertEquals(userEntity, mapper.toEntity(atlasUserAuthDTO));

    }

    @Test
    void testToDTOAtlasUserAuthMapper_ifUserNull_returnNull() {
        Assertions.assertNull(mapper.toDTO(null));
    }
    @Test
    void testToEntityAtlasUserAuthMapper_ifDtoNull_returnNull() {
        Assertions.assertNull(mapper.toEntity(null));

    }



}
