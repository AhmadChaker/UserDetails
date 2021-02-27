package au.com.uniquewebsitehostname.userdetails.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class UpdateUserDetailsRequest {

    @NotNull
    private String title;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String gender;
    @NotNull
    private String employeeId;
    @NotNull
    private AddressDetails address;
}
