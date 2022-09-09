package spending_management_project.po;

import lombok.*;
import org.hibernate.Hibernate;
import spending_management_project.enums.ValidFlag;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = "user")
public class UserProfile implements Serializable, Comparable<UserProfile> {

    @Id
    @Column(name = "user_id")
    private long id;

    private String signature;

    private String firstName;

    private String lastName;

    private String company;

    private String usr;

    private String email;

    private String address;

    private String city;

    private String country;

    private String postalCode;

    private String bio;

    private String avatar;

    @Column(nullable = false)
    @Builder.Default
    private ValidFlag validFlag = ValidFlag.VALID;

    @Column(nullable = false, updatable = false)
    private long createdAt;

    @Column(nullable = false, updatable = false)
    private long createdBy;

    @Column(nullable = false)
    private long lastModifiedAt;

    @Column(nullable = false)
    private long lastModifiedBy;

    @Version
    @Column(nullable = false)
    private int version;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public UserProfile(User user) {
        this.firstName = user.getName();
        this.email = user.getUsr();
        this.usr = user.getUsr();
        this.user = user;
        this.id = user.getId();
    }

    @Override
    public int compareTo(UserProfile o) {
        return usr.compareTo(o.getUsr());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProfile profile = (UserProfile) o;

        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return 1839453005;
    }
}
