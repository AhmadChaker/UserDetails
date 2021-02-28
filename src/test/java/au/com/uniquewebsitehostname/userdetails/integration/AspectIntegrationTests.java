package au.com.uniquewebsitehostname.userdetails.integration;

import au.com.uniquewebsitehostname.userdetails.aspect.LoggingAspect;
import au.com.uniquewebsitehostname.userdetails.dto.AddressDetailsDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.mapper.UserDetailsDtoRequestResponseMapper;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(value="classpath:application-test.properties")
public class AspectIntegrationTests {
    @Autowired
    private UserDetailsDtoRequestResponseMapper mapper;

    private List<ILoggingEvent> logsList;

    @BeforeEach
    public void init() {
        // Setup logger and appender
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);
        logsList = listAppender.list;
    }

    @AfterEach
    public void tearDown() {
        logsList.clear();
    }

    @Test
    public void test_LogBeforeAndAfterAspect_LogsToList() {
        // Setup
        GetUserDetailsServiceDto dto = new GetUserDetailsServiceDto();
        dto.setAddress(new AddressDetailsDto());

        // Act
        mapper.mapDtoToGetUserDetailsResponse(dto);

        // Assert
        assertEquals(2, logsList.size());
        assertTrue(logsList.get(0).getMessage().startsWith("Entry"));
        assertTrue(logsList.get(1).getMessage().startsWith("Exit"));
    }


}
