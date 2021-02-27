package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.exception.ExceptionResponse;
import au.com.uniquewebsitehostname.userdetails.exception.IdValidationException;
import au.com.uniquewebsitehostname.userdetails.exception.UserAuthDetailsNotFoundException;
import au.com.uniquewebsitehostname.userdetails.exception.UserDetailsNotFoundException;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse("An internal server error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public final ResponseEntity<Object> handleHystrixExceptions(HystrixRuntimeException ex, WebRequest request)
    {
        return new ResponseEntity<Object>( new ExceptionResponse("Service unavailable"),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UserAuthDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserAuthNotFoundException(UserAuthDetailsNotFoundException ex,
                                                                        WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse("Username and/or password incorrect"),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserDetailsNotFoundException(UserDetailsNotFoundException ex,
                                                                           WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IdValidationException.class, ConstraintViolationException.class})
    public final ResponseEntity<Object> handleIdValidationException(Exception ex, WebRequest request)
    {
        return new ResponseEntity<Object>(new ExceptionResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
