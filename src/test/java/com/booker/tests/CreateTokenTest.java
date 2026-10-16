package com.booker.tests;

import com.booker.models.AuthCredentials;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("smoke")
@Tag("CreateToken")
public class CreateTokenTest {

    String token;

    public static String getAuthToken() {
        AuthCredentials credentials = new AuthCredentials("admin", "password123");

        return given()
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    @DisplayName("Create Token with True credentials.")
    public void createToken() {
        AuthCredentials credentials = new AuthCredentials("admin", "password123");
        given()
                .header("Accept","application/json")
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue());

    }

    @Test
    @DisplayName("Create Token with False credentials.")
    void createTokenFalse() {
        Faker faker = new Faker();
        String username = faker.name().firstName();
        String password = faker.internet().password();
        AuthCredentials credentials = new AuthCredentials(username, password);
        given()
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"));
    }
}