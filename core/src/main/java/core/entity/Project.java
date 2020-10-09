package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("project")
public class Project {

    @Id
    @Column("project_id")
    private UUID id;

    private String name;
    private String key;

    @Column("organization_id")
    private UUID organizationId;

    @Column("project_type_id")
    private Integer typeId;

    @Column("lead_user_id")
    private String leadId;

    private String img;

    @Column("is_private")
    private Boolean isPrivate;

    @Column("last_modify")
    private ZonedDateTime lastModify; // The last modify of the row in table
}
