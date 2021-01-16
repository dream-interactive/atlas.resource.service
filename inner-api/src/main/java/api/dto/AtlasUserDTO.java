package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtlasUserDTO {
    @NonNull
    private String sub;
    @NonNull
    private String email;
    @NonNull
    private Boolean emailVerified;
    @NonNull
    private String familyName;
    @NonNull
    private String givenName;
    @NonNull
    private String name;
    @NonNull
    private String local;

    private String userPicture;

    private LocalDateTime lastModify;
}
