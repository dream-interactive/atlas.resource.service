package api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtlasUserDTO {

    private String sub;
    private String email;
    private Boolean emailVerified;
    private String familyName;
    private String givenName;
    private String name;
    private String local;
    private String userPicture;
    private LocalDateTime lastModify;
}
