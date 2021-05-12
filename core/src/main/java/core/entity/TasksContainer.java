package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("issues_container")
public class TasksContainer {
    @Id
    @Column("idic") // todo rename to idtc
    private Long idtc;
    @NonNull
    private String name;
    @NonNull
    private UUID idp;

    @Column("can_be_deleted")
    private Boolean canBeDeleted;

    @Transient
    private List<Task> tasks;

    // used for saving order place
    @Column("index_number")
    private Integer indexNumber;

    public TasksContainer(@NonNull String name, @NonNull UUID idp, Boolean canBeDeleted, List<Task> tasks, Integer indexNumber) {
        this.name = name;
        this.idp = idp;
        this.canBeDeleted = canBeDeleted;
        this.tasks = tasks;
        this.indexNumber = indexNumber;
    }
}
