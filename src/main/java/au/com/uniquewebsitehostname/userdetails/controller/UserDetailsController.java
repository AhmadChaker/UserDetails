package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsResponseDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsRequestDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoRequestResponseMapper;
import au.com.uniquewebsitehostname.userdetails.service.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserDetailsController {

    public static final String UserDetailsPath = "/userdetails/";

    @Autowired
    private IUserDetailsService userDetailService;
    @Autowired
    private UserDetailsDtoRequestResponseMapper mapper;

    @GetMapping(UserDetailsPath + "{employeeId}")
    public GetUserDetailsResponseDto getUserDetails(@PathVariable String employeeId) {
        GetUserDetailsServiceDto dto = userDetailService.getUserDetails(employeeId);
        return mapper.map(dto);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(UserDetailsPath + "{employeeId}")
    public void updateUserDetails(@PathVariable String employeeId, @RequestBody UpdateUserDetailsRequestDto request) {
        userDetailService.updateUserDetails(mapper.map(request, employeeId));
    }
}
