package au.com.uniquewebsitehostname.userdetails.service;

import au.com.uniquewebsitehostname.userdetails.dataaccess.dao.IUserDetailsRepository;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserDetailsService implements IUserDetailsService {

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsDtoEntityMapper mapper;

    @Override
    public GetUserDetailsServiceDto getUserDetails(String employeeId) {
        var userDetailsEntity = userDetailsRepository.findByEmployeeId(employeeId);
        if(userDetailsEntity == null) {
            // TODO: Throw not found exception
        }
        return mapper.map(userDetailsEntity);
    }

    @Override
    public void updateUserDetails(UpdateUserDetailsServiceDto userDetailsDto) {
        var userDetailsEntity = userDetailsRepository.findByEmployeeId(userDetailsDto.getOldEmployeeId());
        if(userDetailsEntity == null) {
            // TODO Throw not found exception
        }
        mapper.map(userDetailsDto, userDetailsEntity);
        userDetailsEntity.setLastUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        userDetailsRepository.save(userDetailsEntity);
    }
}
