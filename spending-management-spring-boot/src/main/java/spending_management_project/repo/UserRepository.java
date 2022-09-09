package spending_management_project.repo;



import spending_management_project.enums.ValidFlag;
import spending_management_project.po.User;
import java.util.Optional;


/**
 * User's repository.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 6/25/15
 * @since JDK1.8
 */
public interface UserRepository extends CustomRepository<User, Long> {

  Optional<User> findByIdAndValidFlag(Long id, ValidFlag validFlag);

  Optional<User> findByUsrAndValidFlag(String usr, ValidFlag validFlag);
}
