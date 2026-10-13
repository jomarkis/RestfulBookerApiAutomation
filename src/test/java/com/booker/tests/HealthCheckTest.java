package com.booker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class HealthCheckTest {

    @Test
    @DisplayName("Assure server is online (Ping)")
    public void pingServerTest() {
        given()
                .baseUri("https://restful-booker.herokuapp.com")
                .when()
                .get("/ping")
                .then()
                .statusCode(200);
    }
}