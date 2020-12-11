package core.mapper;

import api.dto.AtlasUserAuthDTO;
import core.entity.AtlasUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;


@Import(AtlasUserAuthMapperImpl.class)
@WebFluxTest(AtlasUserAuthMapper.class)
class AtlasUserAuthMapperTest {

    @Autowired
    private AtlasUserAuthMapper mapper;

    @Test
    void testToDTOAtlasUserAuthMapper_returnDto() {
        AtlasUserAuthDTO atlasUserAuthDTO = new AtlasUserAuthDTO();
        atlasUserAuthDTO.setSub("github|4");
        atlasUserAuthDTO.setName( "Jorik" );
        atlasUserAuthDTO.setEmail( "email@email.com" );
        atlasUserAuthDTO.setEmailVerified( true );

        AtlasUser userEntity = new AtlasUser();

        userEntity.setSub("github|4");
        userEntity.setName( "Jorik" );
        userEntity.setEmail( "email@email.com" );
        userEntity.setEmailVerified( true );

        Assertions.assertEquals(atlasUserAuthDTO, mapper.toDTO(userEntity));

    }

    @Test
    void testToEntityAtlasUserAuthMapper_returnEntity() {
        AtlasUserAuthDTO atlasUserAuthDTO = new AtlasUserAuthDTO();
        atlasUserAuthDTO.setSub("github|4");
        atlasUserAuthDTO.setName( "Jorik" );
        atlasUserAuthDTO.setEmail( "email@email.com" );
        atlasUserAuthDTO.setEmailVerified( true );

        AtlasUser userEntity = new AtlasUser();

        userEntity.setSub("github|4");
        userEntity.setName( "Jorik" );
        userEntity.setEmail( "email@email.com" );
        userEntity.setEmailVerified( true );
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
