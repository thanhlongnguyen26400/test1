package spending_management_project.po;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import spending_management_project.enums.AccountSourceType;
import spending_management_project.listener.PersistentListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Account of user.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 08/02/2017
 * @since JDK1.8
 */
@Entity
@EntityListeners(PersistentListener.class)
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = "user")
public class Account implements Serializable {

  private static final long serialVersionUID = -6004454109313475045L;

  @GenericGenerator(
      name = "accountSequenceGenerator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @Parameter(name = "sequence_name", value = "accounts_seq"),
          @Parameter(name = "initial_value", value = "1"),
          @Parameter(name = "increment_size", value = "1")
      }
  )
  @Id
  @GeneratedValue(generator = "accountSequenceGenerator")
  @Column(updatable = false)
  private long id;

  private String account;

  private AccountSourceType accountSourceType;

  @Column(updatable = false)
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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Account account = (Account) o;

    return Objects.equals(id, account.id);
  }

  @Override
  public int hashCode() {
    return 2083479647;
  }
}
