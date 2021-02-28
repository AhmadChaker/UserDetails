package au.com.uniquewebsitehostname.userdetails.service;

import au.com.uniquewebsitehostname.userdetails.dataaccess.dao.IUserAuthRepository;
import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserAuthEntity;
import au.com.uniquewebsitehostname.userdetails.exception.UserAuthDetailsNotFoundException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private IUserAuthRepository userAuthRepository;

    @HystrixCommand(ignoreExceptions = UserAuthDetailsNotFoundException.class, commandKey = "authHystrix")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthEntity userAuthEntity = userAuthRepository.findByUsername(username);
        if (userAuthEntity == null) {
            throw new UserAuthDetailsNotFoundException(username);
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userAuthEntity.getAuthorities());
        org.springframework.security.core.userdetails.User userDetail =
                new org.springframework.security.core.userdetails.User(userAuthEntity.getUsername(),
                        userAuthEntity.getPassword(), List.of(authority));
        return userDetail;
    }
}
