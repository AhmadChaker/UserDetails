package au.com.uniquewebsitehostname.userdetails.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetUserDetailsServiceDto {
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String employeeId;
    private GetUserDetailsServiceDto.AddressDetails address;

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
