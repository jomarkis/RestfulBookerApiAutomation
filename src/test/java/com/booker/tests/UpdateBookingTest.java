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

public class UpdateBookingTest {
    static String token;

    @BeforeAll
    public static void setUp()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        CreateTokenTest createTokenTest = new CreateTokenTest();
        createTokenTest.createToken();
        token = createTokenTest.token;
    }

    @Test
    @DisplayName("Update Booking with valid data")
    public void updateBookingTest()
    {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get("/booking")
                .then()
                .statusCode(200);
    }
}
