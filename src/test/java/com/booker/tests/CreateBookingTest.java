package com.booker.tests;

import com.booker.models.AuthCredentials;
import com.booker.models.Booking;
import com.booker.models.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateBookingTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    @DisplayName("Create Booking with valid data")
    public void createBookingTest() {
        BookingDates bookingDates = new BookingDates("2024-01-01", "2024-01-10");
        Booking booking = new Booking("John","Markakis",50,true,bookingDates,"Nothing");
        given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo("John"))
                .body("booking.lastname", equalTo("Markakis"))
                .body("booking.totalprice", equalTo(50))
                .body("booking.bookingdates.checkin", equalTo("2024-01-01"))
                .body("booking.bookingdates.checkout", equalTo("2024-01-10"))
                .body("booking.additionalneeds", equalTo("Nothing"));

    }

    @Test
    @DisplayName("Create Booking with different date format (YYYY_MM_DD)")
    public void createBookingInvalidDataTest() {
        BookingDates bookingDates = new BookingDates("2024_01_01", "2024_01_10");
        Booking booking = new Booking("John","Markakis",50,true,bookingDates,"Nothing");
        given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo("John"))
                .body("booking.lastname", equalTo("Markakis"))
                .body("booking.totalprice", equalTo(50))
                .body("booking.bookingdates.checkin", equalTo("2024_01_01"))
                .body("booking.bookingdates.checkout", equalTo("2024_01_10"))
                .body("booking.additionalneeds", equalTo("Nothing"));
    }

    @Test
    @DisplayName("Create Booking with different date format (YYYY/MM/DD)")
    public void createBookingInvalidDataTest2() {
        BookingDates bookingDates = new BookingDates("2024/01/01", "2024/01/10");
        Booking booking = new Booking("John","Markakis",50,true,bookingDates,"Nothing");
        given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo("John"))
                .body("booking.lastname", equalTo("Markakis"))
                .body("booking.totalprice", equalTo(50))
                .body("booking.bookingdates.checkin", equalTo("2024/01/01"))
                .body("booking.bookingdates.checkout", equalTo("2024/01/10"))
                .body("booking.additionalneeds", equalTo("Nothing"));
    }

    @Test
    @DisplayName("Create Booking with invalid data (missing lastname)")
    public void createBookingInvalidDataTest3() {
        BookingDates bookingDates = new BookingDates("2024-01-01", "2024-01-10");
        Booking booking = new Booking("John",null,50,true,bookingDates,"Nothing");
        given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(400);
    }
}