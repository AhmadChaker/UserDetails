package au.com.uniquewebsitehostname.userdetails.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDetailsServiceDto {
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String oldEmployeeId;
    private String employeeId;
    private UpdateUserDetailsServiceDto.AddressDetails address;

    @Getter
    @Setter
    public class AddressDetails {
        private String street;
        private String city;
        private String postcode;
        private String state;
        private String country;
    }
}
