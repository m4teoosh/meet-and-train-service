package pl.sportovo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jakarta.ws.rs.core.UriBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.sportovo.domain.activity.Activity;
import pl.sportovo.domain.activity.dto.ActivityRequest;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.discipline.Discipline;
import pl.sportovo.domain.location.Location;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static pl.sportovo.TestUtils.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivitiesResourceTest {


    @BeforeAll
    public static void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void testPostActivities400NullValues() {
        ActivityRequest activityRequest = new ActivityRequest();
        activityRequest.setDiscipline(Discipline.BOXING);
        activityRequest.setCapacity(33);

        post("/activities", activityRequest)
                .then()
                .statusCode(400)
                .body("errors.size()", is(6));
    }

    @Test
    public void testPostActivities201() {
        ActivityRequest activityRequestBody =  createActivity("bob", "Alighieri Boxing Club", 52.2297700, 21.01178);

        post("/activities", activityRequestBody)
                .then()
                .statusCode(201);
    }

    @Test
    public void testPostActivities400NotExistingLocationAndOwner() {
        ActivityRequest activityRequest = new ActivityRequest();
        activityRequest.setName("Boxing Training");
        activityRequest.setOwnerId(UUID.randomUUID());
        activityRequest.setLocationId(UUID.randomUUID());
        activityRequest.setDiscipline(Discipline.BOXING);
        activityRequest.setCapacity(20);

        post("/activities", activityRequest)
                .then()
                .statusCode(400)
                .body("errors[0].message", is("activity-location-does-not-exist"));
    }

    @Test
    public void testJoinLeaveActivity() {
        // given
        ActivityRequest activityRequestBody = createActivity("bob", "Alighieri Boxing Club", 52.2297700, 21.01178);
        Activity activity = post("/activities", activityRequestBody).as(Activity.class);

        Athlete tom = new Athlete();
        tom.setUsername("tom");
        tom = post("/athletes", tom).as(Athlete.class);

        // when
        post("/activities/" + activity.getId() + "/join/" + tom.getId(), "")
                .then()
                .statusCode(200);

        URI uri = UriBuilder.fromPath("/athletes/{id}/activities")
                .resolveTemplate("id", tom.getId())
                .build();

        // then
        get(uri.toString(), 200)
                .body("size()", is(1));
        delete("/activities/" + activity.getId() + "/leave/" + tom.getId(), 200);

        get(uri.toString(), 200)
                .body("size()", is(0));
    }

    @Test
    public void testFindNearbyActivities() {
        ActivityRequest activityRequestBody = createActivity("john","Alighieri Boxing Club",52.2297700, 21.01178);
        post("/activities", activityRequestBody).as(Activity.class);

        activityRequestBody = createActivity("bob", "Szakal Boxing Club", 52.244339, 21.001006);
        post("/activities", activityRequestBody).as(Activity.class);

        get("/activities/nearby?latitude=52.2297700&longitude=21.01178&radius=10", 200)
                .body("size()", is(1));
    }

    private ActivityRequest createActivity(String ownerName, String locationName, double lat, double lon) {
        Athlete owner = new Athlete();
        owner.setUsername(ownerName);

        owner = post("/athletes", owner).as(Athlete.class);

        Location location = new Location();
        location.setName(locationName);
        location.setLatitude(lat);
        location.setLongitude(lon);

        location = post("/locations", location).as(Location.class);

        ActivityRequest activityRequest = new ActivityRequest();
        activityRequest.setName("Boxing Training");
        activityRequest.setOwnerId(owner.getId());
        activityRequest.setLocationId(location.getId());
        activityRequest.setDiscipline(Discipline.BOXING);
        activityRequest.setCapacity(20);
        activityRequest.setStartDateTime(LocalDateTime.now().plusDays(1).toString());
        activityRequest.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(1).toString());
        return activityRequest;
    }

}
