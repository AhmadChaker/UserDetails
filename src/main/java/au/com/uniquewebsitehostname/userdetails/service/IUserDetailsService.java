package au.com.uniquewebsitehostname.userdetails.service;

import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;

public interface IUserDetailsService {
    GetUserDetailsServiceDto getUserDetails(String employeeId);
    void updateUserDetails(UpdateUserDetailsServiceDto updateUserDetailsServiceDto);
}
