package spending_management_project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spending_management_project.enums.IncomeType;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardVO implements Serializable {
    private long income;
    private long expenditure;
    private long balance_month;
    private double saving_money;
    private String lastUpdate;
    private Map<Object, Map<IncomeType,Long>> graph;
    private Map<IncomeType, Map<Long,Long>> pie;
    private Set<HistoryTypeVO> history_type;
    private Map<String, Map<IncomeType, Long>> line_chart;
}
