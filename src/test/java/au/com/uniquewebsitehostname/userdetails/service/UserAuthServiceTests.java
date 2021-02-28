package au.com.uniquewebsitehostname.userdetails.service;

import au.com.uniquewebsitehostname.userdetails.dataaccess.dao.IUserAuthRepository;
import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserAuthEntity;
import au.com.uniquewebsitehostname.userdetails.exception.IdValidationException;
import au.com.uniquewebsitehostname.userdetails.exception.UserAuthDetailsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAuthServiceTests {
    @Mock
    private IUserAuthRepository userAuthRepository;
    @InjectMocks
    private UserAuthService userAuthService;

    @Test
    public void test_loadUserByUsername_UsernameNotInDatabase_ThrowsUserAuthDetailsException() {
        // Setup
        String username = "achaker";
        when(userAuthRepository.findByUsername(eq(username))).thenReturn(null);

        // Act
        UserAuthDetailsNotFoundException exception = assertThrows(UserAuthDetailsNotFoundException.class, () -> {
            userAuthService.loadUserByUsername(username);
        });

        // Assert
        assertEquals("Auth details not found for username: achaker", exception.getMessage());
        verify(userAuthRepository).findByUsername(eq(username));
    }

    @Test
    public void test_loadUserByUsername_ReturnsDetailsSuccessfully_ThrowsUserAuthDetailsException() {
        // Setup
        String username = "achaker";
        UserAuthEntity authEntity = new UserAuthEntity();
        authEntity.setUsername(username);
        authEntity.setPassword("password123");
        authEntity.setAuthorities("ROLE1");

        when(userAuthRepository.findByUsername(eq(username))).thenReturn(authEntity);

        // Act
        org.springframework.security.core.userdetails.UserDetails details=userAuthService.loadUserByUsername(username);

        // Assert
        assertEquals("achaker",details.getUsername());
        assertEquals("password123",details.getPassword());
        assertEquals("ROLE1",details.getAuthorities().iterator().next().getAuthority());

        verify(userAuthRepository).findByUsername(eq(username));
    }

}
