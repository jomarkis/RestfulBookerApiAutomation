package com.booker.tests;

import com.booker.models.AuthCredentials;
import com.booker.models.Booking;
import com.booker.models.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;

@Tag("UpdateBooking")
public class UpdateBookingTest {
    static String token;
    static Booking booking;
    static int bookingId;

    @BeforeAll
    public static void setUp()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        token = CreateTokenTest.getAuthToken();
        bookingId = GetBookingTest.getFirstBookingId();

        BookingDates bookingDates = new BookingDates("2024-01-01", "2024-01-10");
        booking = new Booking("John","Markakis",50,true,bookingDates,"Nothing");
    }

    @Test
    @DisplayName("Update Booking with valid data with Authorization")
    public void updateBookingTestAuth()
    {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic("admin", "password123")
                .body(booking)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Markakis"))
                .body("totalprice", equalTo(50))
                .body("bookingdates.checkin", equalTo("2024-01-01"))
                .body("bookingdates.checkout", equalTo("2024-01-10"))
                .body("additionalneeds", equalTo("Nothing"));
    }

    @Test
    @DisplayName("Update Booking with valid data with Cookie")
    public void updateBookingTestCookie()
    {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cookie", "token="+token)
                .body(booking)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Markakis"))
                .body("totalprice", equalTo(50))
                .body("bookingdates.checkin", equalTo("2024-01-01"))
                .body("bookingdates.checkout", equalTo("2024-01-10"))
                .body("additionalneeds", equalTo("Nothing"));
    }

    @Test
    @DisplayName("Update Booking with valid data with both Cookie and Authorization")
    public void updateBookingTest()
    {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cookie", "token="+token)
                .auth().preemptive().basic("admin", "password123")
                .body(booking)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Markakis"))
                .body("totalprice", equalTo(50))
                .body("bookingdates.checkin", equalTo("2024-01-01"))
                .body("bookingdates.checkout", equalTo("2024-01-10"))
                .body("additionalneeds", equalTo("Nothing"));

    }

    @AfterEach
    @DisplayName("Check bookingId after update using parameters")
    public void checkBookingIdAfterUpdate()
    {
        given()
                .header("Accept", "application/json")
                .queryParam("firstname", "John")
                .queryParam("lastname", "Markakis")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", not(empty()))
                .body("bookingid", hasItem(bookingId));
    }

    @AfterEach
    @DisplayName("Check booking after update using booking id")
    public void checkBookingAfterUpdate() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Markakis"));
    }
}
