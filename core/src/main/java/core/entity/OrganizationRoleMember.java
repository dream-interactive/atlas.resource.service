package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("org_role_member")
public class OrganizationRoleMember {
    @Column("organization_id")
    private UUID organizationId;
    @Column("member_id")
    private String memberId;
    @Column("org_role_id")
    private Integer userRole;
}
