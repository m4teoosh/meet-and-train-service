package pl.sportovo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.sportovo.domain.discipline.Discipline;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TeamResourceTest {

    @BeforeAll
    public static void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void testListTeams() {
        given()
                .when().get("/teams")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0)); // Ensure we receive an array
    }

    @Test
    public void testCreateTeam() {
        String teamName = "Team Alpha";
        Discipline discipline = Discipline.CALISTHENICS;

        Response response = given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "%s",
                          "discipline": "%s",
                          "isInviteOnly": true
                      }
                      """.formatted(teamName, discipline))
                .when().post("/teams")
                .then()
                .statusCode(201)
                .extract().response();

        String location = response.getHeader("Location");
        assertNotNull(location);

        // Verify the created team
        given()
                .when().get(location)
                .then()
                .statusCode(200)
                .body("name", equalTo(teamName))
                .body("discipline", equalTo(discipline.toString()))
                .body("isInviteOnly", equalTo(true));
    }

    @Test
    public void testCreateDuplicateTeam() {
        String teamName = "Duplicate Team";
        Discipline discipline = Discipline.CYCLING;

        // First creation
        given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "%s",
                          "discipline": "%s",
                          "isInviteOnly": false
                      }
                      """.formatted(teamName, discipline))
                .when().post("/teams")
                .then()
                .statusCode(201);

        // Attempt to create a duplicate
        given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "%s",
                          "discipline": "%s",
                          "isInviteOnly": false
                      }
                      """.formatted(teamName, discipline))
                .when().post("/teams")
                .then()
                .statusCode(400)
                .body("errors[0].message", equalTo("A team with this name already exists!"));
    }

    @Test
    public void testGetTeamById() {
        String teamName = "Team Bravo";
        String discipline = "TENNIS";

        // Create a team
        Response response = given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "%s",
                          "discipline": "%s",
                          "isInviteOnly": false
                      }
                      """.formatted(teamName, discipline))
                .when().post("/teams")
                .then()
                .statusCode(201)
                .extract().response();

        String location = response.getHeader("Location");
        UUID id = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));

        // Retrieve the team by ID
        given()
                .when().get("/teams/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(teamName))
                .body("discipline", equalTo(discipline))
                .body("isInviteOnly", equalTo(false));
    }

    @Test
    public void testUpdateTeam() {
        String originalName = "Team Charlie";
        String updatedName = "Team Delta";
        String discipline = "SWIMMING";

        // Create a team
        Response response = given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "%s",
                          "discipline": "%s",
                          "isInviteOnly": true
                      }
                      """.formatted(originalName, discipline))
                .when().post("/teams")
                .then()
                .statusCode(201)
                .extract().response();

        String location = response.getHeader("Location");
        UUID id = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));

        // Update the team
        given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "%s",
                          "discipline": "%s",
                          "isInviteOnly": false
                      }
                      """.formatted(updatedName, discipline))
                .when().put("/teams/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedName))
                .body("isInviteOnly", equalTo(false));
    }

    @Test
    public void testDeleteTeam() {
        String teamName = "Team Echo";
        String discipline = "VOLLEYBALL";

        // Create a team
        Response response = given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "%s",
                          "discipline": "%s",
                          "isInviteOnly": false
                      }
                      """.formatted(teamName, discipline))
                .when().post("/teams")
                .then()
                .statusCode(201)
                .extract().response();

        String location = response.getHeader("Location");
        UUID id = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));

        // Delete the team
        given()
                .when().delete("/teams/" + id)
                .then()
                .statusCode(204);

        // Verify the team is no longer available
        given()
                .when().get("/teams/" + id)
                .then()
                .statusCode(404);
    }
}
