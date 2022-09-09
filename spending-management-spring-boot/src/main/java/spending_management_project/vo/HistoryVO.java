package spending_management_project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryVO {
    private long id;
    private int type;
    private Long money;
    private String description;
    private String date;
    private long history_type;
}
