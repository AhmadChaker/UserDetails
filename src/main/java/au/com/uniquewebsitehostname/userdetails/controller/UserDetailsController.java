package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsResponseDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsRequestDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoRequestResponseMapper;
import au.com.uniquewebsitehostname.userdetails.service.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserDetailsController {

    @Autowired
    private IUserDetailsService userDetailService;
    @Autowired
    private UserDetailsDtoRequestResponseMapper mapper;

    @GetMapping("/userdetails/{employeeId}")
    public GetUserDetailsResponseDto getUserDetails(@PathVariable String employeeId) {
        GetUserDetailsServiceDto dto = userDetailService.getUserDetails(employeeId);
        return mapper.map(dto);
    }

    @PutMapping("/userdetails/{employeeId}")
    public void updateUserDetails(@PathVariable String employeeId, @RequestBody UpdateUserDetailsRequestDto request) {
        userDetailService.updateUserDetails(mapper.map(request, employeeId));
    }
}
