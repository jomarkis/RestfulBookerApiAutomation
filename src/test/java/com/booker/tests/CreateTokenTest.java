package com.booker.tests;

import com.booker.models.AuthCredentials;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateTokenTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    @DisplayName("Create Token with True credentials.")
    void createToken() {
        AuthCredentials credentials = new AuthCredentials("admin", "password123");
        given()
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