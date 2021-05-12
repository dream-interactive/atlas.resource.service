package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("task")
public class Task {
    @Id
    @Column("idi") // todo rename
    private Long idt;
    @NonNull
    @Column("idic")
    private Long idtc; // TasksContainer Id
    @NonNull
    @Column("index_number")
    private Integer indexNumber; // used for saving order place
    @NonNull
    private String summary;
    @NonNull
    @Column("project_id")
    private UUID idp;
    /*
     * Insert should be run from backend in loop with try / catch ( DataBase error - value exist)

     *   select MAX(key_number) into keyN from issue where key = new.key;
     *   new.key_number := keyN + 1;
     *   return new;
     * */
    @Column("key_number")
    private Long keyNumber;
    @Column("assign_to_user_id")
    private String assignToId; // assign to user
    @NonNull
    @Column("creator_id")
    private String creatorId;
    @Column("checker_id")
    private String checkerId;

    @NonNull
    private String priority; // 'Low', 'Medium', 'High'
    private String description; // html
    private Byte points;
    @Column("close_before_issues")
    private List<Long> closeBeforeIssuesIds;
    @Column("close_after_issues")
    private List<Long> closeAfterIssuesIds;
    @Column("close_with_issues")
    private List<Long> closeWithIssuesIds;
    private List<String> labels;
    @NonNull
    @Column("date_time_s")
    private LocalDateTime dateTimeS; // dateTime start
    @Column("date_time_e")
    private LocalDateTime dateTimeE; // dateTime end
    @Column("last_modify")
    private LocalDateTime dateTimeU; // last update

}
