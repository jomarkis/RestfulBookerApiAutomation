package com.booker.tests;

import com.booker.models.Booking;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("GetBooking")
public class GetBookingTest {

    public static int getFirstBookingId() {
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
                .body("firstname", isA(String.class))
                .body("lastname", isA(String.class))
                .body("totalprice", isA(Number.class))
                .body("depositpaid", isA(Boolean.class))
                .body("bookingdates", isA(java.util.Map.class))
                .body("bookingdates.checkin", isA(String.class))
                .body("bookingdates.checkout", isA(String.class))
                .body("additionalneeds", isA(String.class));
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class GetBookingWithParam {

        Booking booking;
        int firstBookingId;

        @BeforeAll
        public void setUp() {
            this.firstBookingId = getFirstBookingId();

            Booking booking = given()
                    .header("Accept", "application/json")
                    .when()
                    .get("/booking/" + firstBookingId)
                    .as(Booking.class);

            this.booking = booking;
        }

        @Test
        @DisplayName("Retrieve Booking IDs with specific first name")
        public void getBookingIdsWithFirstNameTest() {
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

        @Test
        @DisplayName("Retrieve Booking IDs with specific last name")
        public void getBookingIdsWithLastNameTest() {
            String lastname = booking.getLastname();

            given()
                    .header("Accept", "application/json")
                    .queryParam("lastname", lastname) //booking?lastname=Smith
                    .when()
                    .get("/booking")
                    .then()
                    .statusCode(200)
                    .body("bookingid", not(empty()))
                    .body("bookingid", hasItem(firstBookingId));
        }

        @Test
        @DisplayName("Retrieve Booking IDs with specific checkin date")
        public void getBookingIdsWithCheckinDateTest() {
            String checkinDate = booking.getBookingdates().getCheckin();

            given()
                    .header("Accept", "application/json")
                    .queryParam("checkin", checkinDate) //booking?checkin=2023-01-01
                    .when()
                    .get("/booking")
                    .then()
                    .statusCode(200)
                    .body("bookingid", not(empty()))
                    .body("bookingid", hasItem(firstBookingId));
        }

        @Test
        @DisplayName("Retrieve Booking IDs with specific checkout date")
        public void getBookingIdsWithCheckoutDateTest() {
            String checkoutDate = booking.getBookingdates().getCheckout();

            given()
                    .header("Accept", "application/json")
                    .queryParam("checkout", checkoutDate) //booking?checkout=2023-01-01
                    .when()
                    .get("/booking")
                    .then()
                    .statusCode(200)
                    .body("bookingid", not(empty()))
                    .body("bookingid", hasItem(firstBookingId));
        }
    }
}