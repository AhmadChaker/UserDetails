package au.com.uniquewebsitehostname.userdetails.service;

import au.com.uniquewebsitehostname.userdetails.dataaccess.dao.IUserDetailsRepository;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.exception.UserDetailsNotFoundException;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoEntityMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.sql.Timestamp;

@Service
public class UserDetailsService implements IUserDetailsService {

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Autowired
    Validator validator;

    @Autowired
    private UserDetailsDtoEntityMapper mapper;

    @HystrixCommand(ignoreExceptions = UserDetailsNotFoundException.class)
    @Override
    public GetUserDetailsServiceDto getUserDetails(String employeeId) {
        var userDetailsEntity = userDetailsRepository.findByEmployeeId(employeeId);
        if(userDetailsEntity == null) {
            throw new UserDetailsNotFoundException(employeeId);
        }

        return mapper.map(userDetailsEntity);
    }

    @HystrixCommand(ignoreExceptions = UserDetailsNotFoundException.class)
    @Override
    public void updateUserDetails(UpdateUserDetailsServiceDto userDetailsDto) {
        var userDetailsEntity = userDetailsRepository.findByEmployeeId(userDetailsDto.getOldEmployeeId());
        if(userDetailsEntity == null) {
            throw new UserDetailsNotFoundException(userDetailsDto.getOldEmployeeId());
        }
        mapper.map(userDetailsDto, userDetailsEntity);
        var violations = validator.validate(userDetailsEntity);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        userDetailsEntity.setLastUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        userDetailsRepository.save(userDetailsEntity);
    }
}
