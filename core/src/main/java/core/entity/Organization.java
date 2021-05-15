package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("organization")
public class Organization {
    @Id
    @Column("organization_id")
    private UUID id;
    @NonNull
    private String name;
    @Column("valid_name")
    @NonNull
    private String validName;
    // owner user
    @Column("owner_user_id")
    @NonNull
    private String ownerUserId;
    private String img;

    // The last modify of the row in table
    @Column("last_modify")
    private ZonedDateTime lastModify;
}
