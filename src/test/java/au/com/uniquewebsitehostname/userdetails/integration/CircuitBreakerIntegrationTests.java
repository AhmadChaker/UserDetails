package au.com.uniquewebsitehostname.userdetails.integration;

import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.exception.ErrorCode;
import au.com.uniquewebsitehostname.userdetails.exception.ExceptionResponse;
import com.netflix.config.ConfigurationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(value="classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts= {"classpath:schema.sql", "classpath:data-test.sql"})
public class CircuitBreakerIntegrationTests {
    @Autowired
    TestRestTemplate restTemplate;

    private static final String workingEmployeeId = "0012345698";
    private final String authHeader = "Basic YWNoYWtlckFkbWluOkp1cGl0ZXIyMDIy";

    @BeforeEach
    void setup() {
        resetHystrix();
    }

    @AfterEach
    void teardown() {
        resetHystrix();
    }

    void resetHystrix() {
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.authHystrix.circuitBreaker.forceOpen", false);
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.businessHystrix.circuitBreaker.forceOpen", false);
    }

    @Test
    public void test_BusinessCircuitBreakerTripped_ReturnsHttp503() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.authHystrix.circuitBreaker.forceOpen", false);
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.businessHystrix.circuitBreaker.forceOpen", true);
        // Act
        ResponseEntity<ExceptionResponse> response =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.GET, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE);
        assertEquals(ErrorCode.CIRCUIT_BREAKER_TRIPPED.getErrorCode(), response.getBody().getCode());
    }

    // Ideally we would throw 503 for this as well however SecurityFilters work before the Controller Advice so its 401
    @Test
    public void test_AuthCircuitBreakerTripped_ReturnsHttp401() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.authHystrix.circuitBreaker.forceOpen", true);
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.businessHystrix.circuitBreaker.forceOpen", false);
        // Act
        ResponseEntity<Object> response =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.GET, httpEntity,
                        Object.class);

        // Assert
        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

}
