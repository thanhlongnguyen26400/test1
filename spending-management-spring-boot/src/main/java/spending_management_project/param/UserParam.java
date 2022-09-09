package spending_management_project.param;

import lombok.*;
import org.springframework.http.HttpMethod;
import spending_management_project.annotation.NotNullField;
import spending_management_project.annotation.SizeField;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserParam extends BaseParam{

    private Long id;

    @NotNullField(method = HttpMethod.POST, message = "usr cannot be null.")
    @SizeField(min = 4, max = 50, method = HttpMethod.POST, message = "usr must greater than or equal to 4 and less than or equal to 50.")
    private String usr; // username

    @NotNullField(method = HttpMethod.POST, message = "pwd cannot be null.")
    @SizeField(min = 4, max = 16, method = HttpMethod.POST, message = "pwd must greater than or equal to 4 and less than or equal to 16.")
    private String pwd; // password

    private String name; // user's name

    private String description;

    @NotNullField(method = HttpMethod.POST,message = "role cannot null")
    private String roleId; // role ids string

    private String historyIds;
    public UserParam(String usr) {
        this.usr = usr;
    }
}
