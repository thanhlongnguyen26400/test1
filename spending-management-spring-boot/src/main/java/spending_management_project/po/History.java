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
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "histories")
@EntityListeners({PersistentListener.class, ValidFlagListener.class})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = {"user","historyType"})
public class History implements Serializable {

    @GenericGenerator(
            name = "historySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name",value = "history_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )

    @Id
    @GeneratedValue(generator = "historySequenceGenerator")
    @Column(updatable = false)
    private long id;

    @Column(nullable = false)
    private IncomeType type;

    @Column(nullable = false)
    private long money;

    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "history_type_id",nullable = false)
    private HistoryType  historyType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private ValidFlag validFlag = ValidFlag.VALID;

    @Column(nullable = false, updatable = false)
    private long createdAt;

    @Column(nullable = false)
    private long createdBy;

    @Column(nullable = false)
    private long lastModifiedAt;

    @Column(nullable = false)
    private long lastModifiedBy;

    public History(History history) {
        this.id = history.getId();
        this.type = history.getType();
        this.money = history.getMoney();
        this.description = history.getDescription();
        this.date = history.getDate();
        this.historyType = history.getHistoryType();
        this.user = history.getUser();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        History history = (History) o;

        return Objects.equals(id, history.id);
    }

    @Override
    public int hashCode() {
        return 263621948;
    }
}
