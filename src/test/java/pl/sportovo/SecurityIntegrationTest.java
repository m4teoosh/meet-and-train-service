package pl.sportovo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jakarta.ws.rs.core.UriBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.sportovo.domain.athlete.Athlete;

import java.net.URI;

import static pl.sportovo.TestUtils.get;
import static pl.sportovo.TestUtils.post;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class SecurityIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Test
    public void tryReadOwnerOnlyResourceAsUnauthorized() {
        // given
        Athlete fred = new Athlete();
        fred.setUsername("fred");
        fred = post("/athletes", fred, TestUtils.generateBearerToken()).as(Athlete.class);

        // when
        URI uri = UriBuilder.fromPath("/athletes/{id}/activities")
                .resolveTemplate("id", fred.getId())
                .build();

        // then
        get(uri.toString(), 403, TestUtils.generateBearerToken());
    }

    @Test
    public void tryReadOwnerOnlyResourceAsOwner() {
        // given
        String token = TestUtils.generateBearerToken();
        Athlete ben = new Athlete();
        ben.setUsername("ben");
        ben = post("/athletes", ben, token).as(Athlete.class);

        // when
        URI uri = UriBuilder.fromPath("/athletes/{id}/activities")
                .resolveTemplate("id", ben.getId())
                .build();

        // then
        get(uri.toString(), 200, token);
    }
}
