package com.qa.api;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeeApi {

    private WebDriver driver;

    public EmployeeApi(WebDriver driver) {
        this.driver = driver;
    }

    public Response get(String requestUrl) {
        RequestSpecification request = RestAssured.given().accept("application/json");

   for (Cookie cookie : driver.manage().getCookies()) {
        	request.cookie(cookie.getName(), cookie.getValue());
        }
        return request.get(requestUrl);
    }
}