package spending_management_project.param;

import lombok.*;
import spending_management_project.annotation.NotNullField;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SumaryParam extends BaseParam {
    @NotNullField(message = "field cannot null")
    private String from;
    @NotNullField(message = "field cannot null")
    private String to;


}
