package pl.sportovo;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class TestUtils {

    public static Response post(String path, Object body) {
        return given()
                .when()
                .body(body)
                .contentType(ContentType.JSON)
                .post(path);
    }

    public static ValidatableResponse get(String path, int status) {
        return given()
                .when()
                .get(path)
                .then()
                .statusCode(status);
    }

    public static ValidatableResponse delete(String path, int status) {
        return given()
                .when()
                .contentType(ContentType.JSON)
                .delete(path)
                .then()
                .statusCode(status);
    }
}
