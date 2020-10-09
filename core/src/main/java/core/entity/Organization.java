package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("organization")
public class Organization {

    @Id
    @Column("organization_id")
    private UUID id;
    private String name;
    private String validName;
    // owner user
    private String ownerUserId;

    // The last modify of the row in table
    @Column("last_modify")
    private ZonedDateTime lastModify;

}
