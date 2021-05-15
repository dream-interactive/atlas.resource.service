package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {

    private Long idi;
    private Long idic; // IssuesContainer Id
    // used for saving order place
    private Integer indexNumber;
    private String name;
    private Long assignToId; // assign to user
    private Long creatorId;
    private String description;
    private List<Long> closeBeforeIssues;
    private List<Long> closeAfterIssues;
    private List<Long> closeWithIssues;
    private LocalDateTime dateTimeS;
    private LocalDateTime dateTimeE;
}
