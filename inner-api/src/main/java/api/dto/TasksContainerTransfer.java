package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Maksym Sevriukov.
 * Date: 22.05.2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasksContainerTransfer {
  private Long currentIdtc;
  private Long previousIdtc;
  private List<TaskDTO> currentTasks;
  private List<TaskDTO> previousTasks;
}
