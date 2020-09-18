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
@Table("atlas_user")
public class AtlasUser {

    @Id
    @Column("user_id")
    private UUID id;
    private String sub;
    private String email;

    @Column("last_modify")
    private ZonedDateTime lastModify; // The last modify of the row in table
}
