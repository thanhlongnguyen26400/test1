package spending_management_project.repo;

import spending_management_project.enums.ValidFlag;
import spending_management_project.po.HistoryType;

import java.util.Optional;

public interface HistoryTypeRepository extends CustomRepository<HistoryType, Long> {
    Optional<HistoryType> findByNameAndValidFlag(String name, ValidFlag validFlag);
}
