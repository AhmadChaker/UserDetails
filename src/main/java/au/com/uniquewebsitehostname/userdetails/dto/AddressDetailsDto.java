package au.com.uniquewebsitehostname.userdetails.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressDetailsDto {
    private String street;
    private String city;
    private String postcode;
    private String state;
    private String country;
}
