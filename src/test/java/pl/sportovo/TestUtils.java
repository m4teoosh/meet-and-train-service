package pl.sportovo;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.smallrye.jwt.build.Jwt;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class TestUtils {

    public static String generateBearerToken() {
        return Jwt.subject(UUID.randomUUID().toString())
                .issuer("https://server.example.com")
                .audience("https://service.example.com").sign();
    }

    public static Response post(String path, Object body, String bearerToken) {
        return given()
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken)
                .when()
                .body(body)
                .contentType(ContentType.JSON)
                .post(path);
    }

    public static ValidatableResponse get(String path, int status, String bearerToken) {
        return given()
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken)
                .when()
                .get(path)
                .then()
                .statusCode(status);
    }

    public static ValidatableResponse delete(String path,  String bearerToken, int status) {
        return given()
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken)
                .when()
                .contentType(ContentType.JSON)
                .delete(path)
                .then()
                .statusCode(status);
    }
}
