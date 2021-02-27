package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoRequestResponseMapper;
import au.com.uniquewebsitehostname.userdetails.service.IUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
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
    public GetUserDetailsResponse getUserDetails(@PathVariable String employeeId) {
        GetUserDetailsServiceDto dto = userDetailService.getUserDetails(employeeId);
        return mapper.map(dto);
    }

    @Operation(summary = "Update user details", security = @SecurityRequirement(name = "basicAuth"))
    @Secured("ROLE_ADMIN")
    @PutMapping(UserDetailsPath + "{employeeId}")
    public void updateUserDetails(@PathVariable String employeeId, @Valid @RequestBody UpdateUserDetailsRequest request) {
        userDetailService.updateUserDetails(mapper.map(request, employeeId));
    }
}
