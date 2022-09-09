package spending_management_project.param;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationParam extends BaseParam {
    private long id;

    public Pageable getPageable() {
        PageRequest pageable = PageRequest.of(getPageNo() - 1, getPageSize());
        return pageable;
    }
}
