package core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Created by Maksym Sevriukov.
 * Date: 04.12.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("atlas_exceptions")
public class AtlasException {
    @Id
    private Integer aeid; // atlas exception id
    private String key; // ATLAS, JDK, SQL
    private String section; // project, organization, ticket etc
    @Column("messageinlog")
    private String messageInLog; // message in log
    @Column("messageinthrow")
    private String messageInThrow; // message in throw
    private String title;
    private String description; // description for programmer
}
