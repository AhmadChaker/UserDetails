package au.com.uniquewebsitehostname.userdetails.interceptor;

import au.com.uniquewebsitehostname.userdetails.exception.IdValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IdValidationInterceptorTests {
    @Mock
    private HttpServletRequest servletRequest;
    @InjectMocks
    private IdValidationInterceptor interceptor;

    @Test
    public void test_preHandle_UrlEmpty_ThrowsIdValidationException() {
        // Setup
        String url = "";
        when(servletRequest.getRequestURI()).thenReturn(url);

        // Act
        IdValidationException exception = assertThrows(IdValidationException.class, () -> {
            interceptor.preHandle(servletRequest, null, null);
        });

        // Assert
        assertEquals("No employee id specified", exception.getMessage());
        verify(servletRequest).getRequestURI();
    }

    @Test
    public void test_preHandle_LastUrlPartEmpty_ThrowsIdValidationException() {
        // Setup
        String url = "http://localhost:8080/api/v2/userdetails/";
        when(servletRequest.getRequestURI()).thenReturn(url);

        // Act
        IdValidationException exception = assertThrows(IdValidationException.class, () -> {
            interceptor.preHandle(servletRequest, null, null);
        });

        // Assert
        assertEquals("No employee id specified", exception.getMessage());
        verify(servletRequest).getRequestURI();
    }

    @Test
    public void test_preHandle_EmployeeIdNotAnInteger_ThrowsIdValidationException() {
        // Setup
        String url = "http://localhost:8080/api/v2/userdetails/abcd";
        when(servletRequest.getRequestURI()).thenReturn(url);

        // Act
        IdValidationException exception = assertThrows(IdValidationException.class, () -> {
            interceptor.preHandle(servletRequest, null, null);
        });

        // Assert
        assertEquals("Employee Id validation failed, id:abcd is not an integer", exception.getMessage());
        verify(servletRequest).getRequestURI();
    }

    @Test
    public void test_preHandle_EmployeeIdValidInteger_ReturnsTrue() {
        // Setup
        String url = "http://localhost:8080/api/v2/userdetails/1234";
        when(servletRequest.getRequestURI()).thenReturn(url);

        // Act
        boolean isHandled = interceptor.preHandle(servletRequest, null, null);

        // Assert
        assertTrue(isHandled);
        verify(servletRequest).getRequestURI();
    }
}
