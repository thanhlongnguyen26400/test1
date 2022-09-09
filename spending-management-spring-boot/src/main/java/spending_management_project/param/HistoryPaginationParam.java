package spending_management_project.param;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import spending_management_project.MetaModel.HistoryType_;
import spending_management_project.MetaModel.History_;
import spending_management_project.MetaModel.User_;
import spending_management_project.enums.IncomeType;
import spending_management_project.po.History;
import spending_management_project.tools.DateUtil;

import java.util.Arrays;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPaginationParam extends BaseParam {
    private String type;
    private String history_type;
    private String from;
    private String to;
    private String min;
    private String max;


    public Pageable getPageable() {
        PageRequest pageable = PageRequest.of(getPageNo() - 1, getPageSize());
        if (StringUtils.isNotBlank(getSortBy())) {
            String[] sortKey = StringUtils.split(getSortBy(), ".");
            pageable = pageable.withSort(Sort.by(Sort.Direction.valueOf(sortKey[1]), sortKey[0]));
        }else{
            pageable = pageable.withSort(Sort.by(Sort.Direction.DESC,"id"));
        }
        return pageable;
    }

    public Specification<History> getSpecification(long id) throws Exception {
        Specification<History> specification = Specification.where(getSpecificationUser(id));
        if (StringUtils.isNumeric(type)) {
            System.out.println("type " + type);
            specification = specification.and(getSpecificationType(Integer.parseInt(type)));
        }
        if (StringUtils.isNotBlank(history_type)) {
            System.out.println("history type " + history_type);
            specification = specification
                    .and(Arrays.stream(StringUtils.split(history_type, "-"))
                    .mapToLong(Long::parseLong)
                    .mapToObj(this::getSpecificationHistoryType)
                    .reduce(Specification::or)
                    .orElse(null));
        }
        if (StringUtils.isNotBlank(from)) {
            System.out.println("from " + from);
            specification = specification.and(getSpecificationFromDate(DateUtil.stringToDate(from, "yyyy-MM-dd")));
        }
        if (StringUtils.isNotBlank(to)) {
            System.out.println("to " + to);
            specification = specification.and(getSpecificationToDate(DateUtil.stringToDate(to, "yyyy-MM-dd")));
        }
        if (StringUtils.isNumeric(min)) {
            System.out.println("min " + min);
            specification = specification.and(getSpecificationMin(Long.parseLong(min)));
        }
        if (StringUtils.isNumeric(max)) {
            System.out.println("max" + max);
            specification = specification.and(getSpecificationMax(Long.parseLong(max)));
        }
        return specification;
    }

    private Specification<History> getSpecificationUser(long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.in(root.get(History_.USER).get(User_.ID)).value(id);
    }

    private Specification<History> getSpecificationType(int typeId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.in(root.get(History_.TYPE)).value(IncomeType.parse(typeId));
    }

    private Specification<History> getSpecificationHistoryType(long htid) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.in(root.get(History_.HISTORY_TYPE).get(HistoryType_.ID)).value(htid);
    }

    private Specification<History> getSpecificationFromDate(Date date) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(History_.DATE), date);
    }

    private Specification<History> getSpecificationToDate(Date date) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(History_.DATE), date);
    }

    private Specification<History> getSpecificationMin(long min) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(History_.MONEY), min);
    }

    private Specification<History> getSpecificationMax(long max) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(History_.MONEY), max);
    }
}
