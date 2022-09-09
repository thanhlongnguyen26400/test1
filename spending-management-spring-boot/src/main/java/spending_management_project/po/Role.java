package spending_management_project.po;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import spending_management_project.constant.AuthorityConstant;
import spending_management_project.enums.ValidFlag;
import spending_management_project.listener.PersistentListener;
import spending_management_project.listener.ValidFlagListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Authorized roles, provide for spring security.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 6/23/15
 * @since JDK1.8
 */
@Entity
@Table(name = "roles")
@EntityListeners({PersistentListener.class, ValidFlagListener.class})
//@NamedEntityGraph(name = "Role.resources", attributeNodes = @NamedAttributeNode("resources"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = "users")
public class Role implements Serializable {

  private static final long serialVersionUID = -5193344128221526323L;

  @GenericGenerator(
      name = "roleSequenceGenerator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @Parameter(name = "sequence_name", value = "roles_seq"),
          @Parameter(name = "initial_value", value = "3"),
          @Parameter(name = "increment_size", value = "1")
      }
  )
  @Id
  @GeneratedValue(generator = "roleSequenceGenerator")
  @Column(updatable = false)
  private long id;

  @Column(unique = true, nullable = false, length = 20)
  private String name;

  @Column(length = 500)
  private String description;

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

  @OneToMany(cascade = CascadeType.ALL,mappedBy = "role")
  @ToString.Exclude
  private Set<User> users;

  public Role(String name, String description) {
    this.name = name;
    this.description = description;
  }
  public boolean isAdmin(){
    return name.equalsIgnoreCase(AuthorityConstant.ADMIN);
  }
  public boolean isUer(){
    return name.equalsIgnoreCase(AuthorityConstant.USER);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Role role = (Role) o;

    return Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return 1179619963;
  }
}
