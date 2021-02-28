package au.com.uniquewebsitehostname.userdetails.mapper;

import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.AddressEntity;
import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserDetailEntity;
import au.com.uniquewebsitehostname.userdetails.dto.AddressDetailsDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserDetailsDtoEntityMapperTests {

    private UserDetailsDtoEntityMapper mapper;
    @BeforeEach
    void init() {
        mapper = new UserDetailsDtoEntityMapper();
    }

    @Test
    public void test_mapEntityToGetUserDetailsDto() {
        // Setup
        UserDetailEntity userDetailEntity = new UserDetailEntity();
        userDetailEntity.setId(10);
        userDetailEntity.setEmployeeId("employeeId");
        userDetailEntity.setTitle("MR");
        userDetailEntity.setFirstName("First");
        userDetailEntity.setLastName("Last");
        userDetailEntity.setGender("Gender");
        userDetailEntity.setLastUpdatedDateTime(new Timestamp(1));
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(3);
        addressEntity.setStreet("Street");
        addressEntity.setPostcode("postcode");
        addressEntity.setState("State");
        addressEntity.setCountry("Country");
        userDetailEntity.setAddress(addressEntity);

        // Act
        GetUserDetailsServiceDto dto = mapper.mapEntityToGetUserDetailsDto(userDetailEntity);

        // Assert
        assertEquals("employeeId", dto.getEmployeeId());
        assertEquals("MR", dto.getTitle());
        assertEquals("First", dto.getFirstName());
        assertEquals("Last", dto.getLastName());
        assertEquals("Gender", dto.getGender());
        assertEquals("Street", dto.getAddress().getStreet());
        assertEquals("postcode", dto.getAddress().getPostcode());
        assertEquals("State", dto.getAddress().getState());
        assertEquals("Country", dto.getAddress().getCountry());
    }

    @Test
    public void test_mapUpdateUserDetailsDtoToEntity() {
        // Setup
        UpdateUserDetailsServiceDto updateUserDetailsDto = new UpdateUserDetailsServiceDto();
        updateUserDetailsDto.setEmployeeId("123");
        updateUserDetailsDto.setTitle("MR");
        updateUserDetailsDto.setFirstName("ING");
        updateUserDetailsDto.setLastName("ING Last");
        updateUserDetailsDto.setGender("Male");
        AddressDetailsDto addressDto = new AddressDetailsDto();
        addressDto.setStreet("60 Margaret Street");
        addressDto.setPostcode("2000");
        addressDto.setCity("Sydney");
        addressDto.setState("NSW");
        addressDto.setCountry("Australia");
        updateUserDetailsDto.setAddress(addressDto);

        UserDetailEntity targetEntity = new UserDetailEntity();
        targetEntity.setAddress(new AddressEntity());

        // Act
        mapper.mapUpdateUserDetailsDtoToEntity(updateUserDetailsDto, targetEntity);

        // Assert
        assertEquals("123", targetEntity.getEmployeeId());
        assertEquals("MR", targetEntity.getTitle());
        assertEquals("ING", targetEntity.getFirstName());
        assertEquals("ING Last", targetEntity.getLastName());
        assertEquals("Male", targetEntity.getGender());
        assertEquals("60 Margaret Street", targetEntity.getAddress().getStreet());
        assertEquals("2000", targetEntity.getAddress().getPostcode());
        assertEquals("NSW", targetEntity.getAddress().getState());
        assertEquals("Australia", targetEntity.getAddress().getCountry());
    }
}
