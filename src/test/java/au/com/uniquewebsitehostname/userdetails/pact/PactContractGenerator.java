package au.com.uniquewebsitehostname.userdetails.pact;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.uniquewebsitehostname.userdetails.api.AddressDetails;
import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
public class PactContractGenerator {

    String mainApiPath = "/api/v1/user-details";
    private final String NonPrivelegedAuthHeader = "Basic YWNoYWtlcjpNYXJzMjAyMQ==";

    @Pact(provider = "user_detail_provider", consumer = "user_detail_consumer")
    public RequestResponsePact createPactPut(PactDslWithProvider builder) throws ParseException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Authorization", NonPrivelegedAuthHeader);

        DslPart updateUserDetailsRequest = new PactDslJsonBody()
                .stringType("title", "My title")
                .stringType("firstName", "First")
                .stringType("lastName", "Last")
                .stringType("gender", "Male")
                .stringType("employeeId", "123")
                .object("address")
                .stringType("street", "123 Fake Street")
                .stringType("city", "Sydney")
                .stringType("postcode", "2000")
                .stringType("state", "NSW")
                .stringType("country", "Australia")
                .closeObject();

        DslPart getUserDetailsResponse = new PactDslJsonBody()
                .stringType("title", "My title")
                .stringType("firstName", "First")
                .stringType("lastName", "Last")
                .stringType("gender", "Male")
                .stringType("employeeId", "123")
                .object("address")
                .stringType("street", "123 Fake Street")
                .stringType("city", "Sydney")
                .stringType("postcode", "2000")
                .stringType("state", "NSW")
                .stringType("country", "Australia")
                .closeObject();

        return builder
                .given("An incoming request  to update user details")
                    .uponReceiving("A request to update user details")
                    .path(mainApiPath)
                    .method("PUT")
                    .headers(headers)
                    .body(updateUserDetailsRequest)
                    .willRespondWith()
                    .status(201)
                .given("An incoming request to get user details ")
                    .uponReceiving("A request to update user details")
                    .path(mainApiPath)
                    .method("GET")
                    .headers(headers)
                    .willRespondWith()
                    .body(getUserDetailsResponse)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "user_detail_provider", port = "8080")
    public void runTest() {
        // Setup (UpdateUserDetails)
        UpdateUserDetailsRequest request = new UpdateUserDetailsRequest();
        request.setAddress(new AddressDetails());
        request.setTitle("MR");
        request.setFirstName("First");
        request.setLastName("Last");
        request.setGender("Male");
        request.setEmployeeId("123");
        request.getAddress().setStreet("123 Fake st");
        request.getAddress().setPostcode("2000");
        request.getAddress().setCity("Sydney");
        request.getAddress().setState("NSW");
        request.getAddress().setCountry("Australia");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("Authorization", NonPrivelegedAuthHeader);
        HttpEntity<UpdateUserDetailsRequest> httpEntity = new HttpEntity<UpdateUserDetailsRequest>(request, headers);
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        // Act (UpdateUserDetails)
        ResponseEntity<Object> response = testRestTemplate.exchange("http://localhost:8080" + mainApiPath, HttpMethod.PUT, httpEntity,
                Object.class);

        // Assert (UpdateUserDetails)
        assert (response.getStatusCode().value() == 201);

        // Setup (GetUserDetails)
        HttpEntity<String> getUserDetailsHttpEntity = new HttpEntity<String>(null, headers);

        // Act (GetUserDetails)
        ResponseEntity<GetUserDetailsResponse> getUserDetailsResponse =
                testRestTemplate.exchange("http://localhost:8080" + mainApiPath, HttpMethod.GET,
                        getUserDetailsHttpEntity, GetUserDetailsResponse.class);

        // Assert (GetUserDetails)
        assertEquals("First", getUserDetailsResponse.getBody().getFirstName());
    }

}
