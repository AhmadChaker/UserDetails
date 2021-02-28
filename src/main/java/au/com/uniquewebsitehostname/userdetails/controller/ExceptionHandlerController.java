package au.com.uniquewebsitehostname.userdetails.controller;

import au.com.uniquewebsitehostname.userdetails.aspect.LoggingAspect;
import au.com.uniquewebsitehostname.userdetails.exception.*;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleRemainingExceptions(Exception ex, WebRequest request) {
        return formatResponse("An internal server error has occurred", ex, ErrorCode.GENERIC,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public final ResponseEntity<Object> handleHystrixExceptions(HystrixRuntimeException ex, WebRequest request) {
        return formatResponse("Service unavailable", ex, ErrorCode.CIRCUIT_BREAKER_TRIPPED,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UserAuthDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserAuthNotFoundException(UserAuthDetailsNotFoundException ex,
                                                                        WebRequest request) {
        return formatResponse("Username and/or password incorrect", ex, ErrorCode.USER_AUTH_DETAILS_NOT_FOUND,
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDetailsNotFoundException.class)
    public final ResponseEntity<Object> handleUserDetailsNotFoundException(UserDetailsNotFoundException ex,
                                                                           WebRequest request) {
        return formatResponse(ex, ErrorCode.USER_DETAILS_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IdValidationException.class)
    public final ResponseEntity<Object> handleIdValidationException(Exception ex, WebRequest request) {
        return formatResponse(ex, ErrorCode.ID_VALIDATION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintValidationException(ConstraintViolationException ex,
                                                                            WebRequest request) {
        return formatResponse(ex, ErrorCode.CONSTRAINT_VALIDATION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return formatResponse(ex, ErrorCode.ACCESS_FORBIDDEN, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return formatResponse("Incoming message unreadable", ex, ErrorCode.HTTP_MESSAGE_UNREADABLE,
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return formatResponse("Request arguments not valid", ex, ErrorCode.CONTROLLER_METHOD_ARG_INVALID,
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return formatResponse(ex, ErrorCode.NO_HANDLER_FOUND, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> formatResponse(String message, Exception ex, ErrorCode code, HttpStatus httpStatus) {
        var exceptionResponse = new ExceptionResponse(message, code);
        if(logger == null) {
            logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
        }
        logger.error(exceptionResponse.toString(), ex);
        return new ResponseEntity<Object>(exceptionResponse, httpStatus);
    }

    private ResponseEntity<Object> formatResponse(Exception ex, ErrorCode code, HttpStatus httpStatus) {
        return formatResponse(ex.getMessage(), ex, code, httpStatus);
    }
}
