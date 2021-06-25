package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("org_role_member")
public class OrganizationMember {
    @Id
    @Column("organization_id")
    @NonNull
    private UUID organizationId;
    @Column("member_id")
    @NonNull
    private String memberId;
    @Column("org_role_id")
    @NonNull
    private Integer userRoleId;
}
