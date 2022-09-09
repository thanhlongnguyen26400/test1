package spending_management_project.po;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import spending_management_project.enums.IncomeType;
import spending_management_project.enums.ValidFlag;
import spending_management_project.listener.PersistentListener;
import spending_management_project.listener.ValidFlagListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "notifications")
@EntityListeners({PersistentListener.class, ValidFlagListener.class})
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {
    @GenericGenerator(
            name = "notiSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "notis_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "notiSequenceGenerator")
    @Column(updatable = false)
    private long id;

    private long user_id;

    private IncomeType type;

    private String content;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @Column(nullable = false)
    @Builder.Default
    private ValidFlag validFlag = ValidFlag.VALID;

    @Column(nullable = false, updatable = false)
    private long createdAt;

    @Column(nullable = false, updatable = false)
    private long createdBy;

    @Column(nullable = false)
    private long lastModifiedAt;

    @Column(nullable = false, updatable = true)
    private long lastModifiedBy;

    public boolean isUnRead() {
        return !isRead;
    }
    public void markRead(){
        isRead = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Notification that = (Notification) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 436862861;
    }
}
