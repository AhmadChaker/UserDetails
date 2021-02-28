package au.com.uniquewebsitehostname.userdetails.service;

import au.com.uniquewebsitehostname.userdetails.dataaccess.dao.IUserAuthRepository;
import au.com.uniquewebsitehostname.userdetails.dataaccess.dao.IUserDetailsRepository;
import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.AddressEntity;
import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserDetailEntity;
import au.com.uniquewebsitehostname.userdetails.dto.AddressDetailsDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.exception.UserAuthDetailsNotFoundException;
import au.com.uniquewebsitehostname.userdetails.exception.UserDetailsNotFoundException;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTests {
    @Mock
    private IUserDetailsRepository userDetailsRepository;
    @Spy
    private UserDetailsDtoEntityMapper mapper;
    @Spy
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @InjectMocks
    private UserDetailsService userDetailsService;
    @Mock
    private ConstraintViolation constraintViolation;

    @Test
    public void test_getUserDetails_EmployeeIdDoesntExist_ThrowsUserDetailNotFoundException() {
        // Setup
        String employeeId = "1234";
        when(userDetailsRepository.findByEmployeeId(eq(employeeId))).thenReturn(null);

        // Act
        UserDetailsNotFoundException exception = assertThrows(UserDetailsNotFoundException.class, () -> {
            userDetailsService.getUserDetails(employeeId);
        });

        // Assert
        assertEquals("No user details exist for employeeId: 1234", exception.getMessage());
        verify(userDetailsRepository).findByEmployeeId(eq(employeeId));
    }

    @Test
    public void test_getUserDetails_EmployeeIdExists_SuccessfullyMapped() {
        // Setup
        String employeeId = "1234";
        UserDetailEntity entity = new UserDetailEntity();
        AddressEntity addressEntity = new AddressEntity();
        entity.setAddress(addressEntity);
        when(userDetailsRepository.findByEmployeeId(eq(employeeId))).thenReturn(entity);

        // Act
        GetUserDetailsServiceDto responseDto = userDetailsService.getUserDetails(employeeId);

        // Assert
        verify(userDetailsRepository).findByEmployeeId(eq(employeeId));
        verify(mapper).mapEntityToGetUserDetailsDto(eq(entity));
    }

    @Test
    public void test_updateUserDetails_EmployeeIdDoesntExist_ThrowsUserDetailNotFoundException() {
        // Setup
        String employeeId = "12345";
        UpdateUserDetailsServiceDto userDetailDto = new UpdateUserDetailsServiceDto();
        userDetailDto.setOldEmployeeId(employeeId);

        when(userDetailsRepository.findByEmployeeId(eq(employeeId))).thenReturn(null);

        // Act
        UserDetailsNotFoundException exception = assertThrows(UserDetailsNotFoundException.class, () -> {
            userDetailsService.updateUserDetails(userDetailDto);
        });

        // Assert
        assertEquals("No user details exist for employeeId: 12345", exception.getMessage());
        verify(userDetailsRepository).findByEmployeeId(eq(employeeId));
    }

    @Test
    public void test_updateUserDetails_DtoConstraintViolation_ThrowsConstraintViolationException() {
        // Setup
        String employeeId = "12345";
        UpdateUserDetailsServiceDto userDetailDto = new UpdateUserDetailsServiceDto();
        userDetailDto.setOldEmployeeId(employeeId);
        AddressDetailsDto addressDto = new AddressDetailsDto();
        userDetailDto.setAddress(addressDto);

        // Its oddly difficult to generate ConstraintViolation objects so actual constructed objects will be used
        UserDetailEntity userDetailEntity = new UserDetailEntity();
        userDetailEntity.setId(1);
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(10);
        userDetailEntity.setAddress(addressEntity);

        when(userDetailsRepository.findByEmployeeId(eq(employeeId))).thenReturn(userDetailEntity);

        // Act
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userDetailsService.updateUserDetails(userDetailDto);
        });

        // Assert
        verify(mapper).mapUpdateUserDetailsDtoToEntity(any(UpdateUserDetailsServiceDto.class),
                any(UserDetailEntity.class));
        verify(userDetailsRepository).findByEmployeeId(eq(employeeId));
    }

    @Test
    public void test_updateUserDetails_Success() {
        // Setup
        UpdateUserDetailsServiceDto updateUserDetailsDto = new UpdateUserDetailsServiceDto();
        updateUserDetailsDto.setOldEmployeeId("123");
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

        UserDetailEntity userDetailEntity = new UserDetailEntity();
        userDetailEntity.setId(1);
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(10);
        userDetailEntity.setAddress(addressEntity);

        when(userDetailsRepository.findByEmployeeId(eq("123"))).thenReturn(userDetailEntity);


        // Act
        userDetailsService.updateUserDetails(updateUserDetailsDto);

        // Assert
        verify(mapper).mapUpdateUserDetailsDtoToEntity(any(UpdateUserDetailsServiceDto.class),
                any(UserDetailEntity.class));
        verify(userDetailsRepository).findByEmployeeId(any(String.class));
        verify(userDetailsRepository).save(any(UserDetailEntity.class));
    }
}
