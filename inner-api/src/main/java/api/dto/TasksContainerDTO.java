package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasksContainerDTO {
  private Long idtc;
  private String summary;
  private UUID idp;
  /**
   * Not updatable field in DB, only Container 'DONE' has value as "false".
   *
   * @see core.service.impl.ProjectServiceImpl.create()
   */
  private Boolean canBeDeleted; // not updatable field in DB

  private List<TaskDTO> tasks;
  // used for saving order place
  private Integer indexNumber;
}
