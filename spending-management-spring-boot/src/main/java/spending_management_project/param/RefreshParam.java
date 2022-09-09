package spending_management_project.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import spending_management_project.annotation.NotNullField;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshParam {

    @NotNullField(method = HttpMethod.PUT, message = "refresh token cannot be null.")
    private String refreshToken;
}