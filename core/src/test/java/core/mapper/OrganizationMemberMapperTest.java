package core.mapper;

import api.dto.OrganizationMemberDTO;
import core.entity.OrganizationMember;
import core.exception.CustomRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@Import(OrganizationMemberMapperImpl.class)
@WebFluxTest(OrganizationMemberMapper.class)
class OrganizationMemberMapperTest {

    @Autowired
    private OrganizationMemberMapper mapper;

    @Test
    void testToDTOOrganizationMemberMapper() {
        OrganizationMember orgMember_OWNER = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github1",
                1
        );
        OrganizationMember orgMember_MANAGER = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github2",
                2
        );
        OrganizationMember orgMember_MEMBER = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github3",
                3
        );
        OrganizationMember orgMember_INVALID = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github3",
                4
        );

        OrganizationMemberDTO orgMember_OWNER_DTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github1",
                "OWNER"
        );
        OrganizationMemberDTO orgMember_MANAGER_DTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github2",
                "MANAGER"
        );
        OrganizationMemberDTO orgMember_MEMBER_DTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github3",
                "MEMBER"
        );

        Assertions.assertEquals(orgMember_OWNER_DTO, mapper.toDTO(orgMember_OWNER));
        Assertions.assertEquals(orgMember_MANAGER_DTO, mapper.toDTO(orgMember_MANAGER));
        Assertions.assertEquals(orgMember_MEMBER_DTO, mapper.toDTO(orgMember_MEMBER));
        Assertions.assertThrows(CustomRequestException.class, () -> mapper.toDTO(orgMember_INVALID));
    }

    @Test
    void testToEntityOrganizationMemberMapper() {
        OrganizationMemberDTO orgMember_OWNER_DTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github1",
                "OWNER"
        );
        OrganizationMemberDTO orgMember_MANAGER_DTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github2",
                "MANAGER"
        );
        OrganizationMemberDTO orgMember_MEMBER_DTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github3",
                "MEMBER"
        );
        OrganizationMemberDTO orgMember_INVALID_DTO = new OrganizationMemberDTO(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github3",
                "BOMBER"
        );

        OrganizationMember orgMember_OWNER = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github1",
                1
        );
        OrganizationMember orgMember_MANAGER = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github2",
                2
        );
        OrganizationMember orgMember_MEMBER = new OrganizationMember(
                UUID.fromString("e9e45e28-ba1c-4c4b-8cfd-11f54b23972e"),
                "github3",
                3
        );

        Assertions.assertEquals(orgMember_OWNER, mapper.toEntity(orgMember_OWNER_DTO));
        Assertions.assertEquals(orgMember_MANAGER, mapper.toEntity(orgMember_MANAGER_DTO));
        Assertions.assertEquals(orgMember_MEMBER, mapper.toEntity(orgMember_MEMBER_DTO));
        Assertions.assertThrows(CustomRequestException.class, () -> mapper.toEntity(orgMember_INVALID_DTO));

    }
}