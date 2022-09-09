package spending_management_project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileVO implements Serializable {
    private long id;
    private String signature;

    private String firstName;

    private String lastName;

    private String company;

    private String usr;

    private String email;

    private String address;

    private String city;

    private String country;

    private String postalCode;

    private String bio;

    private String lastUpdate;

    private long income;

    private long expend;

    private String avatar;
}
