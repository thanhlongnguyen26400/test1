package spending_management_project.param;

import lombok.*;
import org.springframework.http.HttpMethod;
import spending_management_project.annotation.NotNullField;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryParam extends BaseParam {

    private long id;

    @NotNullField(method = HttpMethod.POST, message = "type must not null")
    private int type;

    @NotNullField(method = HttpMethod.POST, message = "money must not null")
    private Long money;

    private String description;

    @NotNullField(method = HttpMethod.POST, message = "date must not null")
    private String date;

    @NotNullField(method = HttpMethod.POST, message = "field must not null")
    private long history_type;
}
