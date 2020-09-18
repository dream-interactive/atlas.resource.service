package core.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("atlas_user")
public class AtlasUser {
    @Id
    @Column("id")
    private long id;
    private String username;
    private String password;
    private String email;
}
