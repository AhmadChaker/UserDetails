package au.com.uniquewebsitehostname.userdetails.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AddressDetails {
    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    private String postcode;
    private String state;
    @NotNull
    private String country;
}
