package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssuesContainerDTO {
  private Long idic;
  private String name;
  private UUID idp;
  /**
   * Not updatable field in DB, only Container 'DONE' has value as "false".
   *
   * @see core.service.impl.ProjectServiceImpl.create()
   */
  private Boolean canBeDeleted; // not updatable field in DB

  private List<IssueDTO> issues;
  // used for saving order place
  private Integer indexNumber;
}
