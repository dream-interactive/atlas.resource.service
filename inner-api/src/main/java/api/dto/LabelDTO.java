package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Maksym Sevriukov.
 * Date: 25.05.2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelDTO {
  private Long id;
  private Long idt;
  private String text;
  private String color;
}
