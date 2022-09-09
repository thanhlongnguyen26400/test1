package spending_management_project.tools;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import spending_management_project.MetaModel.History_;
import spending_management_project.MetaModel.User_;
import spending_management_project.po.History;

import java.util.Date;

@Component
public class SpecificationUtil {
    public Specification<History> getSpecificationHistoryUser(long id){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.in(root.get(History_.USER).get(User_.ID)).value(id);
    }
    public Specification<?> getSpecificationHistoryDate(Date from, Date to){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(History_.DATE),from,to);
    }
}
