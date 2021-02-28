package au.com.uniquewebsitehostname.userdetails.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String PACKAGE_PUBLIC_METHODS_CUT =
            "execution(public * au.com.uniquewebsitehostname..*.*(..))";

    @Before(PACKAGE_PUBLIC_METHODS_CUT)
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entry - Class: " + joinPoint.getTarget().getClass().getName() + " Method: "
                + joinPoint.getSignature().getName() + " Args: " + Arrays.asList(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = PACKAGE_PUBLIC_METHODS_CUT, returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("Exit - Class: " + joinPoint.getTarget().getClass().getName() + " Method: "
                + joinPoint.getSignature().getName() + " Return: " + result);
    }
}
