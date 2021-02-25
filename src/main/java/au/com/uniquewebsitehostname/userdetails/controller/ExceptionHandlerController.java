package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.exception.ExceptionResponse;
import au.com.uniquewebsitehostname.userdetails.exception.UserAuthDetailsNotFoundException;
import au.com.uniquewebsitehostname.userdetails.exception.UserDetailsNotFoundException;
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

import java.util.Date;

@ControllerAdvice
@RestController
@EnableWebMvc
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
    {
        ExceptionResponse response = new ExceptionResponse(new Date(), "An internal server error has occurred");
        return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAuthDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserAuthNotFoundException(UserAuthDetailsNotFoundException ex,
                                                                        WebRequest request)
    {
        ExceptionResponse response = new ExceptionResponse(new Date(), "Username and/or password incorrect");
        return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserDetailsNotFoundException(UserDetailsNotFoundException ex,
                                                                           WebRequest request)
    {
        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage());
        return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
    }
}
