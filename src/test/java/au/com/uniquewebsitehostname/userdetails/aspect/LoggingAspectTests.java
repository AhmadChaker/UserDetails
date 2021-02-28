package au.com.uniquewebsitehostname.userdetails.aspect;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.read.ListAppender;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoggingAspectTests {

    @Mock
    private JoinPoint joinPoint;
    @Mock
    private Object target;
    @Mock
    private Signature signature;
    @InjectMocks
    private LoggingAspect loggingAspect;

    private List<ILoggingEvent> logsList;

    @BeforeEach
    void init() {
        // Setup logger and appender
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);
        logsList = listAppender.list;

        // Setup joinPoints
        when(joinPoint.getTarget()).thenReturn(target);
        when(joinPoint.getSignature()).thenReturn(signature);
    }

    @Test
    public void test_logBefore_LogsToFile() {
        // Setup
        when(joinPoint.getArgs()).thenReturn(new String[] {"test1", "test2"});

        // Act
        loggingAspect.logBefore(joinPoint);

        // Assert
        assertEquals(1, logsList.size());
        assertTrue(logsList.get(0).getMessage().startsWith("Entry"));
        verify(joinPoint).getTarget();
        verify(joinPoint).getSignature();
        verify(joinPoint).getArgs();

    }

    @Test
    public void test_logAfter_LogsToFile() {
        // Setup

        // Act
        loggingAspect.logAfter(joinPoint, "Test");

        // Assert
        assertEquals(1, logsList.size());
        assertTrue(logsList.get(0).getMessage().startsWith("Exit"));
        verify(joinPoint).getTarget();
        verify(joinPoint).getSignature();
    }
}
