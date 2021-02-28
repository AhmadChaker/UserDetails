package au.com.uniquewebsitehostname.userdetails.integration;

import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(value="classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts= {"classpath:schema.sql", "classpath:data-test.sql"})
public class AuthenticationIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final String workingEmployeeId = "0012345698";


    @Test
    public void test_NoAuthentication_ReturnsHTTP401() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

        // Act
        ResponseEntity<GetUserDetailsResponse> response =
                restTemplate.exchange("/api/v1/userdetails/123", HttpMethod.GET, httpEntity, GetUserDetailsResponse.class);

        // Assert
        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void test_AuthenticationWorking_ReturnsHTTP200() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic YWNoYWtlcjpNYXJzMjAyMQ==");
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

        // Act
        ResponseEntity<GetUserDetailsResponse> response =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.GET, httpEntity, GetUserDetailsResponse.class);

        // Assert
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
}
