package au.com.uniquewebsitehostname.userdetails.aspect;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LogManager.getLogger(LoggingAspect.class);

    private static final String APP_PACKAGE_WIDE_POINTCUT = "execution(public * au.com.uniquewebsitehostname..*.*(..))";

    @Before(APP_PACKAGE_WIDE_POINTCUT)
    public void logBefore(JoinPoint joinPoint) {
        logger.log(Level.INFO, "Entry - Class: " + joinPoint.getTarget().getClass().getName() + " Method: " +
                joinPoint.getSignature().getName() + " Args: " +Arrays.asList(joinPoint.getArgs()));

    }

    @AfterReturning(pointcut=APP_PACKAGE_WIDE_POINTCUT, returning="result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.log(Level.INFO, "Exit - Class: " + joinPoint.getTarget().getClass().getName() + " Method: " +
                joinPoint.getSignature().getName() + " Return: " + result);
    }

    @AfterThrowing(pointcut=APP_PACKAGE_WIDE_POINTCUT, throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {
        // TODO fix so exception is logged properly (entire stacktrace)
        logger.log(Level.ERROR, "Exception thrown - " + e);
    }
}
