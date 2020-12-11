package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtlasExceptionDTO {
    private Integer aeid; // atlas exception id
    private String key; // ATLAS, JDK, SQL
    private String section; // project, organization, ticket etc
    private String title;
    private String description; // description for programmer
    private boolean privateException;

}
