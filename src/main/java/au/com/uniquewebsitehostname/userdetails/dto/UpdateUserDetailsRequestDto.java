package au.com.uniquewebsitehostname.userdetails.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDetailsRequestDto {
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String employeeId;
    private UpdateUserDetailsRequestDto.AddressDetails address;

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
