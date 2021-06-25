package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationMemberDTO {
    @NonNull
    private UUID organizationId;
    @NonNull
    private String memberId;
    @NonNull
    private String userRole;

}
