package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private UUID id;
    private String name;
    private String key;
    private UUID organizationId;
    private String type;
    private String leadId;
    private String img;
    private Boolean isPrivate;

}
