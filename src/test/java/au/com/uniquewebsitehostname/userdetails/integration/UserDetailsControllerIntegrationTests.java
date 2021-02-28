package au.com.uniquewebsitehostname.userdetails.integration;

import au.com.uniquewebsitehostname.userdetails.api.AddressDetails;
import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import au.com.uniquewebsitehostname.userdetails.exception.ErrorCode;
import au.com.uniquewebsitehostname.userdetails.exception.ExceptionResponse;
import com.netflix.config.ConfigurationManager;
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
public class UserDetailsControllerIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final String workingEmployeeId = "0012345694";
    private final String NonPrivelegedAuthHeader = "Basic YWNoYWtlcjpNYXJzMjAyMQ==";
    private final String PrivelegedAuthHeader = "Basic YWNoYWtlckFkbWluOkp1cGl0ZXIyMDIy";

    @Test
    public void test_GetUserDetailsSuccess_ReturnsHttp200AndUserDetails() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", NonPrivelegedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

        // Act
        ResponseEntity<GetUserDetailsResponse> rawResponse =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.GET, httpEntity,
                        GetUserDetailsResponse.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.OK);
        GetUserDetailsResponse response = rawResponse.getBody();
        assertEquals("TEST1", response.getTitle());
        assertEquals("TEST NAME", response.getFirstName());
        assertEquals("TEST LAST", response.getLastName());
        assertEquals("TESTGENDER", response.getGender());
        assertEquals("9 Test Street", response.getAddress().getStreet());
        assertEquals("Test City", response.getAddress().getCity());
        assertEquals("Test Code", response.getAddress().getPostcode());
        assertEquals("Test State", response.getAddress().getState());
        assertEquals("Test Country", response.getAddress().getCountry());
    }

    @Test
    public void test_UpdateUserDetailsSuccess_ReturnsHttp204() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", PrivelegedAuthHeader);
        UpdateUserDetailsRequest updateUserDetailsRequest = GenerateUpdateUserDetailsRequest();
        HttpEntity<UpdateUserDetailsRequest> httpEntity =
                new HttpEntity<UpdateUserDetailsRequest>(updateUserDetailsRequest, headers);

        // Act
        ResponseEntity<Object> rawResponse =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.PUT, httpEntity,
                        Object.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.NO_CONTENT);
    }

    @Test
    public void test_EmployeeIdNotInteger_IdValidationInterceptorThrows_ReturnsHttp400() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", NonPrivelegedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

        // Act
        ResponseEntity<ExceptionResponse> rawResponse =
                restTemplate.exchange("/api/v1/userdetails/abc", HttpMethod.GET, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.BAD_REQUEST);
        assertEquals(rawResponse.getBody().getCode(), ErrorCode.ID_VALIDATION_FAILED.getErrorCode());
    }

    @Test
    public void test_EmployeeIdNotInDatabase_ReturnsHttp404() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", NonPrivelegedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

        // Act
        ResponseEntity<ExceptionResponse> rawResponse =
                restTemplate.exchange("/api/v1/userdetails/123", HttpMethod.GET, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.NOT_FOUND);
        assertEquals(rawResponse.getBody().getCode(), ErrorCode.USER_DETAILS_NOT_FOUND.getErrorCode());
    }

    @Test
    public void test_WrongAPIPath_ReturnsHttp404() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", NonPrivelegedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

        // Act
        ResponseEntity<ExceptionResponse> rawResponse =
                restTemplate.exchange("/api/v2/userdetails/" + workingEmployeeId, HttpMethod.GET, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.NOT_FOUND);
        assertEquals(rawResponse.getBody().getCode(), ErrorCode.NO_HANDLER_FOUND.getErrorCode());
    }

    @Test
    public void test_UpdateUserDetailsControllerValidationFailed_ReturnsHttp400() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", PrivelegedAuthHeader);
        UpdateUserDetailsRequest updateUserDetailsRequest = GenerateUpdateUserDetailsRequest();
        updateUserDetailsRequest.setFirstName(null);
        HttpEntity<UpdateUserDetailsRequest> httpEntity =
                new HttpEntity<UpdateUserDetailsRequest>(updateUserDetailsRequest, headers);

        // Act
        ResponseEntity<ExceptionResponse> rawResponse =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.PUT, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.BAD_REQUEST);
        assertEquals(ErrorCode.CONTROLLER_METHOD_ARG_INVALID.getErrorCode(), rawResponse.getBody().getCode());
    }

    @Test
    public void test_UpdateUserDetailsControllerEntityValidationFailed_ReturnsHttp400() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", PrivelegedAuthHeader);
        UpdateUserDetailsRequest updateUserDetailsRequest = GenerateUpdateUserDetailsRequest();
        updateUserDetailsRequest.setTitle("MORETHAN10CHARACTERSLONG");
        HttpEntity<UpdateUserDetailsRequest> httpEntity =
                new HttpEntity<UpdateUserDetailsRequest>(updateUserDetailsRequest, headers);

        // Act
        ResponseEntity<ExceptionResponse> rawResponse =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.PUT, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.BAD_REQUEST);
        assertEquals(ErrorCode.CONSTRAINT_VALIDATION_FAILED.getErrorCode(), rawResponse.getBody().getCode());
    }

    @Test
    public void test_PostHttpCall_ReturnsHttp405() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", PrivelegedAuthHeader);
        UpdateUserDetailsRequest updateUserDetailsRequest = GenerateUpdateUserDetailsRequest();
        HttpEntity<UpdateUserDetailsRequest> httpEntity =
                new HttpEntity<UpdateUserDetailsRequest>(updateUserDetailsRequest, headers);

        // Act
        ResponseEntity<ExceptionResponse> rawResponse =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.POST, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(rawResponse.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED);
        assertEquals(ErrorCode.METHOD_NOT_ALLOWED.getErrorCode(), rawResponse.getBody().getCode());
    }

    private UpdateUserDetailsRequest GenerateUpdateUserDetailsRequest() {
        UpdateUserDetailsRequest request = new UpdateUserDetailsRequest();
        request.setEmployeeId(workingEmployeeId);
        request.setFirstName("first");
        request.setLastName("last");
        request.setTitle("mr");
        AddressDetails addressRequest = new AddressDetails();
        addressRequest.setStreet("street");
        addressRequest.setPostcode("2000");
        addressRequest.setCity("Sydney");
        addressRequest.setState("NSW");
        addressRequest.setCountry("Australia");
        request.setAddress(addressRequest);
        return request;
    }
}
