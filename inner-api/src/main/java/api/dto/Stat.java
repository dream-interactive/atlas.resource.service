package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Maksym Sevriukov.
 * Date: 05.06.2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stat {
  private UUID pid;
  private List<TaskDTO> tasks;
  private LocalDate date;
}
