package core.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
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
    private String email;
    @Column("email_verified")
    private Boolean emailVerified;
    @Column("family_name")
    private String familyName;
    @Column("given_name")
    private String givenName;
    private String name;

    private String local;

    @Column("picture")
    private String userPicture;

    @Column("last_modify")
    private LocalDateTime lastModify;

}
