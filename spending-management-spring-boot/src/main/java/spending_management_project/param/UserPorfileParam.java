package spending_management_project.param;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPorfileParam extends BaseParam{
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

}
