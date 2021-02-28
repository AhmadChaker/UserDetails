package au.com.uniquewebsitehostname.userdetails.mapper;

import au.com.uniquewebsitehostname.userdetails.api.AddressDetails;
import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import au.com.uniquewebsitehostname.userdetails.dto.AddressDetailsDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserDetailsDtoRequestResponseMapperTests {
    private UserDetailsDtoRequestResponseMapper mapper;
    @BeforeEach
    public void init() {
        mapper = new UserDetailsDtoRequestResponseMapper();
    }

    @Test
    public void test_mapDtoToGetUserDetailsResponse() {
        // Setup
        GetUserDetailsServiceDto getUserDetailDto = new GetUserDetailsServiceDto();
        getUserDetailDto.setEmployeeId("employeeId");
        getUserDetailDto.setTitle("MR");
        getUserDetailDto.setFirstName("First");
        getUserDetailDto.setLastName("Last");
        getUserDetailDto.setGender("Gender");
        AddressDetailsDto addressDto = new AddressDetailsDto();
        addressDto.setStreet("Street");
        addressDto.setPostcode("postcode");
        addressDto.setState("State");
        addressDto.setCountry("Country");
        getUserDetailDto.setAddress(addressDto);

        // Act
        GetUserDetailsResponse response = mapper.mapDtoToGetUserDetailsResponse(getUserDetailDto);

        // Assert
        assertEquals("employeeId", response.getEmployeeId());
        assertEquals("MR", response.getTitle());
        assertEquals("First", response.getFirstName());
        assertEquals("Last", response.getLastName());
        assertEquals("Gender", response.getGender());
        assertEquals("Street", response.getAddress().getStreet());
        assertEquals("postcode", response.getAddress().getPostcode());
        assertEquals("State", response.getAddress().getState());
        assertEquals("Country", response.getAddress().getCountry());
    }

    @Test
    public void test_mapUpdateUserDetailsDtoToEntity() {
        // Setup
        UpdateUserDetailsRequest updateUserDetailsRequest = new UpdateUserDetailsRequest();
        updateUserDetailsRequest.setEmployeeId("123");
        updateUserDetailsRequest.setTitle("MR");
        updateUserDetailsRequest.setFirstName("ING");
        updateUserDetailsRequest.setLastName("ING Last");
        updateUserDetailsRequest.setGender("Male");
        AddressDetails addressRequest = new AddressDetails();
        addressRequest.setStreet("60 Margaret Street");
        addressRequest.setPostcode("2000");
        addressRequest.setCity("Sydney");
        addressRequest.setState("NSW");
        addressRequest.setCountry("Australia");
        updateUserDetailsRequest.setAddress(addressRequest);
        String oldEmployeeId = "Old 123";

        // Act
        UpdateUserDetailsServiceDto responseDto =
                mapper.mapUpdateUserDetailsRequestToDto(updateUserDetailsRequest, oldEmployeeId);

        // Assert
        assertEquals("123", responseDto.getEmployeeId());
        assertEquals("Old 123", responseDto.getOldEmployeeId());
        assertEquals("MR", responseDto.getTitle());
        assertEquals("ING", responseDto.getFirstName());
        assertEquals("ING Last", responseDto.getLastName());
        assertEquals("Male", responseDto.getGender());
        assertEquals("60 Margaret Street", responseDto.getAddress().getStreet());
        assertEquals("2000", responseDto.getAddress().getPostcode());
        assertEquals("NSW", responseDto.getAddress().getState());
        assertEquals("Australia", responseDto.getAddress().getCountry());
    }
}
