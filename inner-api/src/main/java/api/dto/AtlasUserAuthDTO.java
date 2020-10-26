package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtlasUserAuthDTO {

    private String sub;
    private String nickname;
    private String name;
    private String picture;
    private String email;
    private Boolean emailVerified;
    private ZonedDateTime updatedAt;
}
