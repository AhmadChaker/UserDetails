package au.com.uniquewebsitehostname.userdetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class UserDetailsApplication {
    public static void main(final String[] args) {
        SpringApplication.run(UserDetailsApplication.class, args);
    }
}
