package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.aspect.LoggingAspect;
import au.com.uniquewebsitehostname.userdetails.exception.*;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerTests {

    private List<ILoggingEvent> logsList;

    @InjectMocks
    private ExceptionHandlerController controllerAdvice;

    @BeforeEach
    void init() {
        // Setup logger and appender
        Logger logger = (Logger) LoggerFactory.getLogger(ExceptionHandlerController.class);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);
        logsList = listAppender.list;

        // Setup Controller Advice
        controllerAdvice = new ExceptionHandlerController();
    }

    @Test
    public void test_handleRemainingExceptions_ReturnsGenericCodeAndHTTP500() {
        // Setup
        Exception ex = new Exception("test");

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleRemainingExceptions(ex, null);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCode.GENERIC.getErrorCode(), ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleHystrixExceptions_ReturnsCircuitBreakerCodeAndHTTP503() {
        // Setup
        HystrixRuntimeException ex = new HystrixRuntimeException(null, null,null,null,null);

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleHystrixExceptions(ex, null);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(ErrorCode.CIRCUIT_BREAKER_TRIPPED.getErrorCode(), ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleUserAuthNotFoundException_ReturnsUserAuthDetailsNotFoundCodeAndHTTP401() {
        // Setup
        UserAuthDetailsNotFoundException ex = new UserAuthDetailsNotFoundException("test");

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleUserAuthNotFoundException(ex, null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorCode.USER_AUTH_DETAILS_NOT_FOUND.getErrorCode(), ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleUserDetailsNotFoundException_ReturnsUserDetailsNotFoundCodeAndHTTP404() {
        // Setup
        UserDetailsNotFoundException ex = new UserDetailsNotFoundException("test");

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleUserDetailsNotFoundException(ex, null);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorCode.USER_DETAILS_NOT_FOUND.getErrorCode(), ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleIdValidationException_ReturnsIdValidationFailedCodeAndHTTP400() {
        // Setup
        IdValidationException ex = new IdValidationException("test");

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleIdValidationException(ex, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.ID_VALIDATION_FAILED.getErrorCode(), ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleConstraintValidationException_ReturnsConstraintValidationFailedCodeAndHTTP400() {
        // Setup
        ConstraintViolationException ex = new ConstraintViolationException(null);

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleConstraintValidationException(ex, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.CONSTRAINT_VALIDATION_FAILED.getErrorCode(),
                ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleAccessDeniedException_ReturnsAccessForbiddenCodeAndHTTP403() {
        // Setup
        AccessDeniedException ex = new AccessDeniedException("test");

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleAccessDeniedException(ex, null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(ErrorCode.ACCESS_FORBIDDEN.getErrorCode(),
                ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleHttpMessageNotReadable_ReturnsHttpMessageUnreadableCodeAndHTTP400() {
        // Setup
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("test");

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleHttpMessageNotReadable(ex, null, null, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.HTTP_MESSAGE_UNREADABLE.getErrorCode(),
                ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleMethodArgumentNotValid_ReturnsMethodArgumentNotValidAndHTTP400() throws NoSuchMethodException {
        // Setup
        Method method = String.class.getMethod("lastIndexOf", String.class);
        HandlerMethod handlerMethod = new HandlerMethod(new String(), method);
        MethodParameter methodReturnType = handlerMethod.getReturnType();

        Object toValidate = new Object();
        BindingResult result = new BeanPropertyBindingResult(toValidate, "toValidate");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodReturnType, result);

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleMethodArgumentNotValid(ex, null, null, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.CONTROLLER_METHOD_ARG_INVALID.getErrorCode(),
                ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }

    @Test
    public void test_handleNoHandlerFoundException_ReturnsHandlerNotFoundCodeAndHTTP404() {
        // Setup
        NoHandlerFoundException ex = new NoHandlerFoundException(null, null, null);

        // Act
        ResponseEntity<Object> response =  controllerAdvice.handleNoHandlerFoundException(ex, null, null, null);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorCode.NO_HANDLER_FOUND.getErrorCode(),
                ((ExceptionResponse)response.getBody()).getCode());
        assertEquals(1, logsList.size());
    }
}
