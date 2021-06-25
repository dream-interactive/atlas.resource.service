package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {

    private UUID id;
    @NonNull
    private String name;
    @NonNull
    private String validName;
    @NonNull
    private String ownerUserId;
    private String img;

}
