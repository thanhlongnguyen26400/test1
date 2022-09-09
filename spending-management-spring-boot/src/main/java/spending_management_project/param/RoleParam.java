package spending_management_project.param;

import lombok.*;
import org.springframework.http.HttpMethod;
import spending_management_project.annotation.NotNullField;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleParam extends BaseParam{
    private Long id;

    @NotNullField(method = HttpMethod.POST, message = "name cannot be null.")
    private String name; // role's name
    private String description;
}
