package au.com.uniquewebsitehostname.userdetails.pact;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import org.apache.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PactFolder("pacts")
@Provider("user_detail_provider")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts= {"classpath:schema.sql", "classpath:data-test.sql"})
public class PactTest {
    private final String PrivelegedAuthHeader = "Basic YWNoYWtlckFkbWluOkp1cGl0ZXIyMDIy";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));

    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context, HttpRequest request) {
        request.addHeader("Authorization", PrivelegedAuthHeader);
        context.verifyInteraction();
    }

    @State("A request to get user details")
    public void toGetUserDetails() {

    }

    @State("A request to update user details")
    public void toUpdateUserDetails() {
    }

}
