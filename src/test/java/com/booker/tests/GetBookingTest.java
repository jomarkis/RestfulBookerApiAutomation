package com.booker.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetBookingTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    @DisplayName("Retrieve all booking IDs")
    public void getAllBookingsTest() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", not(empty()));
    }

    @Test
    @DisplayName("Retrieve a specific booking by ID dynamically")
    public void getBookingByIdTest() {
        // Fetch existing IDs first to avoid hardcoded IDs
        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/booking");

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        int firstBookingId = bookingIds.get(0);

        // Fetch booking details using dynamic ID
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + firstBookingId)
                .then()
                .statusCode(200)
                .body("firstname", notNullValue())
                .body("lastname", notNullValue())
                .body("totalprice", greaterThanOrEqualTo(0))
                .body("bookingdates.checkin", notNullValue());
    }
}