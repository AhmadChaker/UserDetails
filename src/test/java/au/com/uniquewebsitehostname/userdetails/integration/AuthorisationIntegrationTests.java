package au.com.uniquewebsitehostname.userdetails.integration;

import au.com.uniquewebsitehostname.userdetails.api.AddressDetails;
import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import au.com.uniquewebsitehostname.userdetails.exception.ErrorCode;
import au.com.uniquewebsitehostname.userdetails.exception.ExceptionResponse;
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
public class AuthorisationIntegrationTests {
    @Autowired
    TestRestTemplate restTemplate;

    private static final String workingEmployeeId = "0012345698";

    private final String NonPrivelegedAuthHeader = "Basic YWNoYWtlcjpNYXJzMjAyMQ==";
    private final String PrivelegedAuthHeader = "Basic YWNoYWtlckFkbWluOkp1cGl0ZXIyMDIy";

    @Test
    public void test_GetUserDetails_NonPrivelegedAuthHeader_ReturnsHTTP200() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", NonPrivelegedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);

        // Act
        ResponseEntity<GetUserDetailsResponse> response =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.GET, httpEntity,
                        GetUserDetailsResponse.class);

        // Assert
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void test_UpdateUserDetails_NonPrivelegedAuthHeader_ReturnsHTTP403() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", NonPrivelegedAuthHeader);
        UpdateUserDetailsRequest request = GenerateUpdateUserDetailsRequest();
        HttpEntity<UpdateUserDetailsRequest> httpEntity = new HttpEntity<UpdateUserDetailsRequest>(request, headers);

        // Act
        ResponseEntity<ExceptionResponse> response =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.PUT, httpEntity,
                        ExceptionResponse.class);

        // Assert
        assertTrue(response.getStatusCode() == HttpStatus.FORBIDDEN);
        assertEquals(ErrorCode.ACCESS_FORBIDDEN.getErrorCode(), response.getBody().getCode());
    }

    @Test
    public void test_UpdateUserDetails_PrivelegedAuthHeader_ReturnsHTTP204() {
        // Setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", PrivelegedAuthHeader);
        UpdateUserDetailsRequest request = GenerateUpdateUserDetailsRequest();
        HttpEntity<UpdateUserDetailsRequest> httpEntity = new HttpEntity<UpdateUserDetailsRequest>(request, headers);

        // Act
        ResponseEntity<Object> response =
                restTemplate.exchange("/api/v1/userdetails/" + workingEmployeeId, HttpMethod.PUT, httpEntity,
                        Object.class);

        // Assert
        assertTrue(response.getStatusCode() == HttpStatus.NO_CONTENT);
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
