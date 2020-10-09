package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {

    private UUID id;
    private String name;
    private String validName;
    private String ownerUserId;
    private String img;

}
