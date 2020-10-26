package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represent the User's profile entity of Auth0
 * */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_profile")
public class AtlasUser {

    @Id
    @Column("sub_user_id")
    private String sub;
    @Column("nickname")
    private String nickname;
    private String name;
    private String picture;
    private String email;

    @Column("email_verified")
    private Boolean emailVerified;

  @Column("last_modify")
  private ZonedDateTime updatedAt;
}
