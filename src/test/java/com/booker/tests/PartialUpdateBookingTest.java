package com.booker.tests;

import com.booker.models.Booking;
import com.booker.models.BookingDates;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Tag("PartialUpdateBooking")
public class PartialUpdateBookingTest {

    static String token;
    static int bookingId;
    static Booking booking;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        token = CreateTokenTest.getAuthToken();
        bookingId = GetBookingTest.getFirstBookingId();

        BookingDates bookingDates = new BookingDates("2024-01-01", "2024-01-10");
        booking = new Booking("John", "Markakis", 50, true, bookingDates, "Nothing");
    }

    @DisplayName("Partial Update Booking with valid data with Authorization")
    @Test
    public void partialUpdateBookingValidData() {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body(booking)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Markakis"))
                .body("totalprice", equalTo(50))
                .body("bookingdates.checkin", equalTo("2024-01-01"))
                .body("bookingdates.checkout", equalTo("2024-01-10"))
                .body("additionalneeds", equalTo("Nothing"));
    }

    @DisplayName("Partial Update Booking with valid data with Cookie")
    @Test
    public void partialUpdateBookingValidData2() {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Markakis"))
                .body("totalprice", equalTo(50))
                .body("bookingdates.checkin", equalTo("2024-01-01"))
                .body("bookingdates.checkout", equalTo("2024-01-10"))
                .body("additionalneeds", equalTo("Nothing"));
    }

    @DisplayName("Partial Update Booking with no data")
    @Test
    public void partialUpdateBookingNoData() {
        Booking firstbooking = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .when()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(firstbooking.getFirstname()))
                .body("lastname", equalTo(firstbooking.getLastname()))
                .body("totalprice", equalTo(firstbooking.getTotalprice()))
                .body("bookingdates.checkin", equalTo(firstbooking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(firstbooking.getBookingdates().getCheckout()))
                .body("additionalneeds", equalTo(firstbooking.getAdditionalneeds()));
    }

    @DisplayName("Partial Update Booking firstname only")
    @Test
    public void partialUpdateBookingFirstnameOnly() {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body("{\"firstname\": \"Jimm\"}")
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Jimm"));
    }

    @DisplayName("Partial Update Booking dates only")
    @Test
    public void partialUpdateBookingDatesOnly() {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body(java.util.Map.of("bookingdates", booking.getBookingdates()))
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("bookingdates.checkin", equalTo("2024-01-01"))
                .body("bookingdates.checkout", equalTo("2024-01-10"));
    }

    @DisplayName("Partial Update Booking checkin date only")
    @Test
    public void partialUpdateBookingCheckInDateOnly() {
        String payload = """
                {
                    "bookingdates": {
                        "checkin": "2024-01-03"
                    }
                }
                """;

        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body(payload)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("bookingdates.checkin", equalTo("2024-01-03"));

    }

    @DisplayName("Partial Update Booking checkout date only")
    @Test
    public void partialUpdateBookingCheckOutDateOnly() {
        String payload = """
                {
                    "bookingdates": {
                        "checkout": "2024-01-12"
                    }
                }
                """;

        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body(payload)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("bookingdates.checkout", equalTo("2024-01-12"));

    }

    @Test
    @DisplayName("Partial Update Booking with invalid checkin > checkout")
    public void partialUpdateBookingWithInvalidCheckinGreaterThanCheckout() {
        String payload = """
                {
                    "bookingdates": {
                        "checkin": "2024-01-03"
                    }
                }
                """;

        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body(payload)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("bookingdates.checkin", equalTo("2024-01-03"));

        payload = """
                {
                    "bookingdates": {
                        "checkout": "2024-01-02"
                    }
                }
                """;

        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body(payload)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .statusCode(400);
    }
}
