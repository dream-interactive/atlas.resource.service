package core.mapper;

import api.dto.OrganizationMemberDTO;
import core.entity.OrganizationMember;
import core.exception.CustomRequestException;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrganizationMemberMapper {

    @Mapping(source = "userRoleId", target = "userRole", qualifiedByName = "userRoleIdToUserRole")
    OrganizationMemberDTO toDTO(OrganizationMember entity);

    @Mapping(source = "userRole", target = "userRoleId", qualifiedByName = "userRoleToUserRoleId")
    OrganizationMember toEntity(OrganizationMemberDTO dto);

    @Named("userRoleIdToUserRole")
    default String userRoleIdToUserRole(Integer userRoleId) {
        switch (userRoleId) {
            case 1:
                return "OWNER";
            case 2:
                return "MANAGER";
            case 3:
                return "MEMBER";
            default:
                throw new CustomRequestException(String.format("ERROR ATLAS-10: Invalid organization member role id - %d", userRoleId), HttpStatus.NOT_FOUND);
        }
    }

    @Named("userRoleToUserRoleId")
    default Integer userRoleToUserRoleId(String userRole) {
        switch (userRole) {
            case "OWNER":
                return 1;
            case "MANAGER":
                return 2;
            case "MEMBER":
                return 3;
            default:
                throw new CustomRequestException(String.format("ERROR ATLAS-11: Invalid organization member role - %s", userRole), HttpStatus.NOT_FOUND);
        }
    }

}
