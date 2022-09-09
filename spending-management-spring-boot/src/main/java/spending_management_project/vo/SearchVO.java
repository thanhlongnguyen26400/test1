package spending_management_project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spending_management_project.po.UserProfile;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchVO implements Serializable {
    private long id;
    private String name;

    public SearchVO(UserProfile profile) {
        this.id = profile.getId();
        this.name = profile.getUsr();
    }
}
