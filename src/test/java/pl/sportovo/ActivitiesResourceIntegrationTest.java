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
import pl.sportovo.domain.activity.Activity;
import pl.sportovo.domain.activity.dto.ActivityInput;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.discipline.Discipline;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static pl.sportovo.TestUtils.*;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class ActivitiesResourceIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void testPostActivities400NullValues() {
        ActivityInput activityInput = new ActivityInput();
        activityInput.setDiscipline(Discipline.BOXING);
        activityInput.setCapacity(33);

        post("/activities", activityInput, generateBearerToken())
                .then()
                .statusCode(400)
                .body("errors.size()", is(5));
    }

    @Test
    public void testPostActivities201() {
        ActivityInput activityInputBody = TestResource.createActivity("bob", "Alighieri Boxing Club", 52.2297700, 21.01178, generateBearerToken());

        post("/activities", activityInputBody, generateBearerToken())
                .then()
                .statusCode(201);
    }

    @Test
    public void testPostActivities400NotExistingLocationAndOwner() {
        ActivityInput activityInput = new ActivityInput();
        activityInput.setName("Boxing Training");
        activityInput.setOwnerId(UUID.randomUUID());
        activityInput.setLocationId(UUID.randomUUID());
        activityInput.setDiscipline(Discipline.BOXING);
        activityInput.setCapacity(20);
        activityInput.setStartDateTime(LocalDateTime.now().plusDays(1).toString());
        activityInput.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(1).toString());

        post("/activities", activityInput, generateBearerToken())
                .then()
                .statusCode(400)
                .body("errors[0].message", is("activity-location-does-not-exist"));
    }

    @Test
    public void testJoinLeaveActivity() {
        // given
        String ownerBearerToken = generateBearerToken();
        ActivityInput activityInputBody = TestResource.createActivity("jennifer", "Alighieri Boxing Club", 52.2297700, 21.01178, ownerBearerToken);
        Activity activity = post("/activities", activityInputBody, ownerBearerToken).as(Activity.class);

        String georgeToken = generateBearerToken();
        Athlete george = new Athlete();
        george.setUsername("george");
        george = post("/athletes", george, georgeToken).as(Athlete.class);

        // when
        post("/activities/" + activity.getId() + "/join/", "", georgeToken)
                .then()
                .statusCode(200);

        URI uri = UriBuilder.fromPath("/athletes/{id}/activities")
                .resolveTemplate("id", george.getId())
                .build();

        // then
        get(uri.toString(), 200, georgeToken)
                .body("size()", is(1));

        delete("/activities/" + activity.getId() + "/leave",  georgeToken, 200);

        get(uri.toString(), 200, georgeToken)
                .body("size()", is(0));
    }

    @Test
    public void testJoinFullActivity() {
        // given
        String ownerBearerToken = generateBearerToken();
        ActivityInput activityInputBody = TestResource.createActivity("greg", "Alighieri Boxing Club", 52.2297700, 21.01178, ownerBearerToken);
        activityInputBody.setCapacity(2);
        Activity activity = post("/activities", activityInputBody, ownerBearerToken).as(Activity.class);

        String danutaBearerToken = generateBearerToken();
        Athlete danuta = new Athlete();
        danuta.setUsername("danuta");
        post("/athletes", danuta, danutaBearerToken);

        // when
        post("/activities/" + activity.getId() + "/join", "", danutaBearerToken)
                .then()
                .statusCode(200);

        String mendezBearerToken = generateBearerToken();
        Athlete mendez = new Athlete();
        mendez.setUsername("mendez");
        post("/athletes", mendez, mendezBearerToken);

        post("/activities/" + activity.getId() + "/join", "", mendezBearerToken)
                .then()
                .statusCode(200);

        String johnBearerToken = generateBearerToken();
        Athlete john = new Athlete();
        john.setUsername("john");
        post("/athletes", john, johnBearerToken);

        // then
        post("/activities/" + activity.getId() + "/join", "", johnBearerToken)
                .then()
                .statusCode(400)
                .body("errors[0].message", is("activity-already-full"));
    }


    @Test
    public void testFindNearbyActivities() {
        String ownerBearerToken = generateBearerToken();
        ActivityInput activityInputBody = TestResource.createActivity("anatoly", "Alighieri Boxing Club", 52.2297700, 21.01178, ownerBearerToken);
        post("/activities", activityInputBody, ownerBearerToken).as(Activity.class);

        String owner2BearerToken = generateBearerToken();
        activityInputBody = TestResource.createActivity("frank", "Szakal Boxing Club", 52.244339, 21.001006, owner2BearerToken);
        post("/activities", activityInputBody, ownerBearerToken).as(Activity.class);

        get("/activities/nearby?latitude=52.2297700&longitude=21.01178&radius=10", 200, generateBearerToken())
                .body("size()", is(2));
    }


}
