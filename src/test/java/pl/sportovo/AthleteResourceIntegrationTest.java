package pl.sportovo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.sportovo.domain.athlete.Athlete;

import static org.hamcrest.Matchers.is;
import static pl.sportovo.TestUtils.generateBearerToken;
import static pl.sportovo.TestUtils.post;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class AthleteResourceIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void testRegisterAthlete201() {
        Athlete athlete = new Athlete();
        athlete.setUsername("tom");
        athlete.setBio("Tom is a boxer");

        post("/athletes", athlete, generateBearerToken())
                .then()
                .statusCode(201)
                .body("username", is("tom"))
                .body("bio", is("Tom is a boxer"));
    }

    @Test
    public void testPostAthletes400NullValues() {
        Athlete athlete = new Athlete();

        post("/athletes", athlete, generateBearerToken())
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostAthletes400UsernameTooShort() {
        Athlete athlete = new Athlete();
        athlete.setUsername("to");

        post("/athletes", athlete, generateBearerToken())
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostAthletes400UsernameTooLong() {
        Athlete athlete = new Athlete();
        athlete.setUsername("1234567890123456789012345678901234567890123456789012345678901234567890");

        post("/athletes", athlete, generateBearerToken())
                .then()
                .statusCode(400)
                .body("errors[0].message", is("length must be between 3 and 20"));
    }

    @Test
    public void testPostAthletes400UsernameExists() {
        Athlete athlete = new Athlete();
        athlete.setUsername("tom");

        post("/athletes", athlete, generateBearerToken())
                .then()
                .statusCode(201);

        post("/athletes", athlete, generateBearerToken())
                .then()
                .statusCode(400)
                .body("errors[0].message", is("username-exists"));
    }

}
