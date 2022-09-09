package spending_management_project.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Custom repository.
 * Extends the {@link JpaRepository} and {@link JpaSpecificationExecutor}
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 10/29/15
 * @since JDK1.8
 */
@NoRepositoryBean public interface CustomRepository<T, ID extends Serializable>
    extends JpaSpecificationExecutor<T>, JpaRepository<T, ID> {

}
