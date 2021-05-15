package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by Maksym Sevriukov.
 * Date: 24.03.2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long idt;
    @NonNull
    private Long idtc; // TasksContainer Id
    @NonNull
    private Integer indexNumber; // used for saving order place
    @NonNull
    private String summary;
    @NonNull

    private UUID idp;
    /*
     * Insert should be run from backend in loop with try / catch ( DataBase error - value exist)
     *   select MAX(key_number) into keyN from issue where key = new.key;
     *   new.key_number := keyN + 1;
     *   return new;
     * */
    private Long keyNumber;
    private String assignToId; // assign to user
    @NonNull
    private String creatorId;
    private String checkerId;

    @NonNull
    private String priority; // 'Low', 'Medium', 'High'
    private String description; // Json
    private Byte points;
    private List<Long> closeBeforeTasks;
    private List<Long> closeAfterTasks;
    private List<Long> closeWithTasks;
    private List<String> labels;
    @NonNull
    private LocalDateTime dateTimeS; // dateTime start
    private LocalDateTime dateTimeE; // dateTime end
    private LocalDateTime dateTimeU; // last update

}
