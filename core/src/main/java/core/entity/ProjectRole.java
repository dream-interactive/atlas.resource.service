package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Maksym Sevriukov.
 * Date: 15.04.2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("role_in_project")
public class ProjectRole {

  @Id
  @Column("id")
  private Integer idpr;

  @Column("role")
  @NonNull
  private String role;
}
