package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.exception.*;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse("An internal server error has occurred",
                ErrorCode.GENERIC), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public final ResponseEntity<Object> handleHystrixExceptions(HystrixRuntimeException ex, WebRequest request)
    {
        return new ResponseEntity<Object>( new ExceptionResponse("Service unavailable",
                ErrorCode.CIRCUIT_BREAKER_TRIPPED), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UserAuthDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserAuthNotFoundException(UserAuthDetailsNotFoundException ex,
                                                                        WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse("Username and/or password incorrect",
                ErrorCode.USER_AUTH_DETAILS_NOT_FOUND), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserDetailsNotFoundException(UserDetailsNotFoundException ex,
                                                                           WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse(ex.getMessage(),
                ErrorCode.USER_DETAILS_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IdValidationException.class)
    public final ResponseEntity<Object> handleIdValidationException(Exception ex, WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse(ex.getMessage(),
                ErrorCode.ID_VALIDATION_FAILED), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintValidationException(Exception ex, WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse(ex.getMessage(),
                ErrorCode.CONSTRAINT_VALIDATION_FAILED), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse(ex.getMessage(),
                ErrorCode.ACCESS_DENIED), HttpStatus.UNAUTHORIZED);
    }
}
