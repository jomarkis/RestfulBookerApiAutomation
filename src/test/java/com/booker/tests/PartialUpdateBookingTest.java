package com.booker.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class PartialUpdateBookingTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }
}
