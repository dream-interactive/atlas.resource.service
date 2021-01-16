package core.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
    @NonNull
    private String sub;
    @NonNull
    private String email;
    @Column("email_verified")
    @NonNull
    private Boolean emailVerified;
    @Column("family_name")
    @NonNull
    private String familyName;
    @Column("given_name")
    @NonNull
    private String givenName;
    @NonNull
    private String name;
    @NonNull
    private String local;

    @Column("picture")
    private String userPicture;

    @Column("last_modify")
    private LocalDateTime lastModify;

}
