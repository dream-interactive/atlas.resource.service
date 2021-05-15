package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private UUID idp;
    @NonNull
    private String name;
    @NonNull
    private String key;
    @NonNull
    private UUID organizationId;
    @NonNull
    private Integer type;
    @NonNull
    private String leadId;

    private String img;
    @NonNull
    private Boolean isPrivate;

    private List<String> labels;

}
