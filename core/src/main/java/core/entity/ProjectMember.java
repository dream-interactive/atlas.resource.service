package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("project_role_member")
public class ProjectMember {

    @Column("project_id")
    @NonNull
    private UUID idp;
    @Column("role_id")
    @NonNull
    private Integer roleId;
    @Column("member_id")
    @NonNull
    private String sub;
}
