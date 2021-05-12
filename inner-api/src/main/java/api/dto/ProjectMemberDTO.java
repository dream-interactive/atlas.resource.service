package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 15.04.2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberDTO {
  private UUID idp;
  private String role;
  private AtlasUserDTO user;
}
