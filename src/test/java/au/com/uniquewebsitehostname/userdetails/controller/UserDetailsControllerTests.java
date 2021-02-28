package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoRequestResponseMapper;
import au.com.uniquewebsitehostname.userdetails.service.IUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsControllerTests {

    @Mock
    private IUserDetailsService userDetailsService;
    @Mock
    private UserDetailsDtoRequestResponseMapper mapper;
    @InjectMocks
    private UserDetailsController controller;

    @Test
    public void test_getUserDetails() {
        // Setup
        String employeeId = "123";
        GetUserDetailsResponse sampleResponse = new GetUserDetailsResponse();
        GetUserDetailsServiceDto sampleDto = new GetUserDetailsServiceDto();
        when(userDetailsService.getUserDetails(eq(employeeId))).thenReturn(sampleDto);
        when(mapper.mapDtoToGetUserDetailsResponse(eq(sampleDto))).thenReturn(sampleResponse);

        // Act
        ResponseEntity<GetUserDetailsResponse> response = controller.getUserDetails(employeeId);

        // Assert
        assertEquals(sampleResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userDetailsService).getUserDetails(eq(employeeId));
        verify(mapper).mapDtoToGetUserDetailsResponse(eq(sampleDto));
    }

    @Test
    public void test_updateUserDetails() {
        // Setup
        String employeeId = "345";
        UpdateUserDetailsRequest updateUserDetailRequest = new UpdateUserDetailsRequest();
        UpdateUserDetailsServiceDto updateUserDetailDto = new UpdateUserDetailsServiceDto();
        when(mapper.mapUpdateUserDetailsRequestToDto(eq(updateUserDetailRequest), eq(employeeId))).thenReturn(updateUserDetailDto);

        // Act
        ResponseEntity response = controller.updateUserDetails(employeeId, updateUserDetailRequest);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mapper).mapUpdateUserDetailsRequestToDto(eq(updateUserDetailRequest), eq(employeeId));
        verify(userDetailsService).updateUserDetails(eq(updateUserDetailDto));
    }
}
