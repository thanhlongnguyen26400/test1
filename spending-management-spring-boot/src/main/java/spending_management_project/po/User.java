package spending_management_project.po;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spending_management_project.enums.ValidFlag;
import spending_management_project.listener.PersistentListener;
import spending_management_project.listener.ValidFlagListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Authorized users, provide for spring security oauth2.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 6/23/15
 * @see {@link UserDetails}
 * @since JDK1.8
 */
@Entity
@Table(name = "users")
@EntityListeners({PersistentListener.class, ValidFlagListener.class})
@NamedEntityGraph(name = "User.role", attributeNodes = @NamedAttributeNode("role"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = {"role", "accounts", "histories"})
public class User implements UserDetails {

    private static final long serialVersionUID = 2680591198337929454L;

    @GenericGenerator(
            name = "userSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "users_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "userSequenceGenerator")
    @Column(updatable = false)
    private long id;

    @Column(length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String usr;

    @Column(nullable = false, length = 200)
    private String pwd;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAccountNonExpiredAlias = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAccountNonLockedAlias = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isCredentialsNonExpiredAlias = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isEnabledAlias = true;

    @Column(nullable = false)
    @Builder.Default
    private ValidFlag validFlag = ValidFlag.VALID;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Last login time
    @Builder.Default
    private long lastLoginAt = System.currentTimeMillis();

    // Last login IP address
    private String ip;

    @Column(nullable = false, updatable = false)
    private long createdAt;

    @Column(nullable = false)
    private long createdBy;

    @Column(nullable = false)
    private long lastModifiedAt;

    @Column(nullable = false)
    private long lastModifiedBy;

    @Version
    @Column(nullable = false)
    private int version;

    //  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
//  @JoinTable(name = "users_has_roles",
//      joinColumns = {@JoinColumn(name = "user_id")},
//      inverseJoinColumns = {@JoinColumn(name = "role_id")})
//  private Set<Role> roles;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<Account> accounts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.MERGE)
    @ToString.Exclude
    private Set<History> histories;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserProfile userProfile;

    public User(@NonNull User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.usr = user.getUsr();
        this.pwd = user.getPwd();
        this.role = user.getRole();
    }

    /**
     * Get the authorities.
     *
     * @return GrantedAuthorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
//    getRoles()
//        .forEach(role -> role.getResources()
//            .forEach(resource -> authorities.add(new SimpleGrantedAuthority(resource.getName()))));
        return authorities;
    }

    @Override
    public String getUsername() {
        return getUsr();
    }

    @Override
    public String getPassword() {
        return getPwd();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpiredAlias();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLockedAlias();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpiredAlias();
    }

    @Override
    public boolean isEnabled() {
        return isEnabledAlias();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 562048007;
    }
}
