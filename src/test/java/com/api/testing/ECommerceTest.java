package com.api.testing;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.api.testing.Common.ConfigReader;
import com.api.testing.Pojo.LoginRequest;
import com.api.testing.Pojo.LoginResponse;
import com.api.testing.Pojo.CreateOrderRequest.CreateOrderReq;
import com.api.testing.Pojo.CreateOrderRequest.OrderDetails;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class ECommerceTest {

    private static final int List = 0;

    public static void main(String []args){

    // Login and Auth Generation
       RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
       .setContentType(ContentType.JSON).build();

       LoginRequest loginRequest=new LoginRequest();
    //    loginRequest.setUserEmail(ConfigReader.get("user.email"));
    //    loginRequest.setUserPassword(ConfigReader.get("user.password"));
        loginRequest.setUserEmail("ranajoy@gmail.com");
        loginRequest.setUserPassword("TestUser@1234");

       RequestSpecification reqLogin=given().spec(req).body(loginRequest);

       LoginResponse response=reqLogin.when().log().all().post("api/ecom/auth/login")
       .then().assertThat().statusCode(200).extract().as(LoginResponse.class);

       System.out.println("Message :"+response.getMessage());
       String userId=response.getUserId();
       System.out.println("UserId :"+userId);
       String token=response.getToken();
       System.out.println("Token :"+token);
    
    // CreateProduct
       RequestSpecification addProductBase=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
       .addHeader("Authorization",token).build();

       RequestSpecification addProductRequest=given().spec(addProductBase)
       .param("productName","Laptop").param("productAddedBy",userId)
       .param("productSubCategory","shirts").param("productCategory","fashion")
       .param("productPrice","11500").param("productDescription","Macbook")
       .param("productFor","unisex").multiPart("productImage",new File("//users//ranajoydas//downloads//unknown.jpeg"))
       .log().all();

       String addProductResponse=addProductRequest.when().post("api/ecom/product/add-product").
       then().log().all()
       .assertThat().statusCode(201).extract().asString();

       JsonPath js =new JsonPath(addProductResponse);
       String productId=js.get("productId");

    // CreateOrder
       RequestSpecification createOrderBaseReq=new RequestSpecBuilder().addHeader("Authorization", token)
       .setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();

       OrderDetails order=new OrderDetails();
       order.setCountry("India");
       order.setProductOrderedId(productId);

       List<OrderDetails> orders=new ArrayList<>();
       orders.add(order);

       CreateOrderReq createOrderReq=new CreateOrderReq();
       createOrderReq.setOrders(orders);;

       RequestSpecification createOrderRequest=given().spec(createOrderBaseReq).body(createOrderReq).log().all();

       String responseForCreateOrder=createOrderRequest.when().post("/api/ecom/order/create-order")
       .then().assertThat().statusCode(201).log().all().extract().asString();
        
    // DeleteOrder
       RequestSpecification deleteProductBaseReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
       .setContentType(ContentType.JSON).addHeader("Authorization", token).build().log().all();

       String deleteProductReq=given().spec(deleteProductBaseReq).pathParam("productId", productId)
       .when().delete("/api/ecom/product/delete-product/{productId}")
       .then().assertThat().statusCode(200).log().all().extract().asString();

    }
}
