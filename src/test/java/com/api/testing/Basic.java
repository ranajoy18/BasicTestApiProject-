package com.api.testing;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Basic {

    public static void main (String[]args){

        RequestSpecification req= RestAssured.given().baseUri("https://restful-booker.herokuapp.com")
        .contentType("application/json")
        .log().all();

        String token=generateToken();
        System.out.println("token :"+token);

        Response response= req.when()
        .get("booking");

        response.then()
        .assertThat()
        .statusCode(200)
        .log().all();
    }

    public static String generateToken(){
          
        return given().log().all().baseUri("https://restful-booker.herokuapp.com")
        .contentType("application/json")
        .body("""
                        {
                          "username": "admin",
                          "password": "password123"
                        }
                        """)
        .when()
        .post("/auth")
        .then().log().all()
        .statusCode(200)
        .extract().path("token");
            
    }
}
