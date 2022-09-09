package spending_management_project.param;

import lombok.*;
import org.springframework.http.HttpMethod;
import spending_management_project.annotation.NotNullField;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryTypeParam extends BaseParam{
    private Long id;
    @NotNullField(method = HttpMethod.POST, message = "name cannot be null")
    private String name;
    private String description;
    private String color;
}
