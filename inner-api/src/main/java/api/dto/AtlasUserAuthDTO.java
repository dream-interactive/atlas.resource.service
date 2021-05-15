package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtlasUserAuthDTO {
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

}
