package pl.sportovo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import pl.sportovo.domain.activity.Activity;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.discipline.Discipline;
import pl.sportovo.domain.location.Location;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.keyStore;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceTest {


    @BeforeAll
    public static void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void testPostLocation() {
        Location l1 = new Location();
        l1.setLatitude(52.2297700);
        l1.setLongitude(21.01178);


    }

    @Test
    @Order(1)
    public void testPostAthletes() {
        Athlete a1 = new Athlete();
        a1.setUsername("Jeremy");

        post("/athletes", a1, 201);

        post("/athletes", a1, 400)
        .body(containsString("Athlete with this username already exists!"));

    }

    @Test
    public void testPostActivities() {
        Activity a = new Activity();
        a.setDiscipline(Discipline.BOXING);
        a.setCapacity(33);
        a.setOwner(null);
        a.setLocation(null);

        post("/activities", a, 400)
        .body("errors.size()", is(3));
    }

    @Test
    public void getActivities() {
        given()
                .when()
                .get("/activities")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    private ValidatableResponse post(String path, Object body, int status) {
        return given()
                .when()
                .body(body)
                .contentType(ContentType.JSON)
                .post(path)
                .then()
                .statusCode(status);
    }
}
