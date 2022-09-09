package spending_management_project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spending_management_project.enums.IncomeType;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationVO implements Serializable {
    private long id;
    private long user_id;
    private IncomeType type;
    private String content;
    private String lastUpdate;
}
