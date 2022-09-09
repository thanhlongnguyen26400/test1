package spending_management_project.po;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import spending_management_project.enums.ValidFlag;
import spending_management_project.listener.PersistentListener;
import spending_management_project.listener.ValidFlagListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "history_type")
@EntityListeners({PersistentListener.class, ValidFlagListener.class})
@Getter
@Setter

@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = "histories")
public class HistoryType implements Serializable {

    @GenericGenerator(
            name = "historyTypeGeneratorSequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "history_type_seq"),
                    @Parameter(name = "initial_value", value = "5"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )

    @Id
    @GeneratedValue(generator = "historyTypeGeneratorSequence")
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String color;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "historyType")
    @ToString.Exclude
    private Set<History> histories;

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

    public HistoryType(HistoryType historyType) {
        this.name = historyType.getName();
        this.description = historyType.getDescription();
        this.color = historyType.getColor();
        this.histories = historyType.getHistories();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<History> getHistories() {
        return this.histories;
    }

    public void setHistories(History history) {
        this.histories.add(history);
    }

    public ValidFlag getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(ValidFlag validFlag) {
        this.validFlag = validFlag;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(long lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HistoryType type = (HistoryType) o;

        return Objects.equals(id, type.id);
    }

    @Override
    public int hashCode() {
        return 421633360;
    }
}
