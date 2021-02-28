package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoRequestResponseMapper;
import au.com.uniquewebsitehostname.userdetails.service.IUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserDetailsController {

    public static final String UserDetailsPath = "api/v1/userdetails/";

    @Autowired
    private IUserDetailsService userDetailService;
    @Autowired
    private UserDetailsDtoRequestResponseMapper mapper;

    @Operation(summary = "Get user details", security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping(UserDetailsPath + "{employeeId}")
    public ResponseEntity<GetUserDetailsResponse> getUserDetails(@PathVariable String employeeId) {
        GetUserDetailsServiceDto dto = userDetailService.getUserDetails(employeeId);
        return new ResponseEntity<GetUserDetailsResponse>(mapper.mapDtoToGetUserDetailsResponse(dto), HttpStatus.OK) ;
    }

    @Operation(summary = "Update user details", security = @SecurityRequirement(name = "basicAuth"))
    @Secured("ROLE_ADMIN")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping(UserDetailsPath + "{employeeId}")
    public ResponseEntity updateUserDetails(@PathVariable String employeeId, @Valid @RequestBody UpdateUserDetailsRequest request) {
        userDetailService.updateUserDetails(mapper.mapUpdateUserDetailsRequestToDto(request, employeeId));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
