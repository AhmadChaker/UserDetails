package au.com.uniquewebsitehostname.userdetails.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetUserDetailsResponse {
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String employeeId;
    private AddressDetails address;
}
