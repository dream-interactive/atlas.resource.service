package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

/** Created by Maksym Sevriukov. Date: 19.03.2021 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("task_comments")
public class TaskComment {
  @Id
  private Long idc;
  @NonNull
  private Long idi;
  @NonNull
  private String text;
  @NonNull
  @Column("is_edited")
  private Boolean isEdited;
  @NonNull
  @Column("author_id")
  private String authorId;
}
