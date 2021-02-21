package au.com.uniquewebsitehostname.userdetails.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserDetailsResponseDto {
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String employeeId;
    private GetUserDetailsResponseDto.AddressDetails address;

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
