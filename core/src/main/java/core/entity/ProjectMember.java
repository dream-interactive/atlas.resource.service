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
@Table("project_role_member")
public class ProjectMember {
    @Column("project_id")
    private UUID projectId;
    @Column("role_id")
    private Integer roleId;
    @Column("member_id")
    private String memberId;
}
