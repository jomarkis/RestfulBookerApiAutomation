package com.booker.tests;

import com.booker.models.Booking;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetBookingTest {

    public int getFirstBookingId() {
        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/booking");

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        return bookingIds.get(0);
    }

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
        int firstBookingId = getFirstBookingId();

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

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class GetBookingWithParam {

        @BeforeAll
        public void setUp() {
            int firstBookingId = getFirstBookingId();

            Booking booking = given()
                    .header("Accept", "application/json")
                    .when()
                    .get("/booking/" + firstBookingId)
                    .as(Booking.class);

            String firstname = booking.getFirstname();
        }

        @Test
        @DisplayName("Retrieve Booking IDs with specific first name")
        public void getBookingIdsWithFirstNameTest() {
            int firstBookingId = getFirstBookingId();

            Booking booking = given()
                    .header("Accept", "application/json")
                    .when()
                    .get("/booking/" + firstBookingId)
                    .as(Booking.class);

            String firstname = booking.getFirstname();

            given()
                    .header("Accept", "application/json")
                    .queryParam("firstname", firstname) //booking?firstname=Sally
                    .when()
                    .get("/booking")
                    .then()
                    .statusCode(200)
                    .body("bookingid", not(empty()))
                    .body("bookingid", hasItem(firstBookingId));
        }
    }
}