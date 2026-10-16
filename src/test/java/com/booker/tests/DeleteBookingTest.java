package com.booker.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Tag("DeleteBooking")
public class DeleteBookingTest {
    static String token;
    static int bookingId;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        token = CreateTokenTest.getAuthToken();
    }

    @BeforeEach
    public void beforeEach() {
        bookingId = GetBookingTest.getFirstBookingId();
    }

    @DisplayName("Delete Booking with valid data with Cookie")
    @Test
    public void deleteBookingValidDataCookie() {
        given()
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(200);
    }

    @DisplayName("Delete Booking with valid data with Cookie.(Defect-masking)")
    @Test
    public void deleteBookingValidDataCookie2() {
        given()
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(201)
                .body(equalTo("Created"));

        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Delete Booking with valid data with Authorization")
    public void deleteBookingValidDataAuth() {
        given()
                .auth().preemptive().basic("admin", "password123")
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Delete Booking with valid data with Authorization.(Defect-masking)")
    public void deleteBookingValidDataAuth2() {
        given()
                .auth().preemptive().basic("admin", "password123")
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(201)
                .body(equalTo("Created"));

        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(404);
    }
}
