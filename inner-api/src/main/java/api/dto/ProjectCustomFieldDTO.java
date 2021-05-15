package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

/**
 * Created by Maksym Sevriukov.
 * Date: 21.03.2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCustomFieldDTO {
    private Long idpcf;
    @NonNull
    private UUID projectId;
    @NonNull
    private String fieldName;
    @NonNull
    private String projectCustomFieldsType;
    private String description;
}
