package com.qa.api.fetch.apiHandler;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiHandler {

	public Response getResponsePost(String baseURL, String requestBody, String ClientID) throws IOException {

		RestAssured.baseURI = baseURL;
		Response response = given().header("Authorization", "")
				.header("client_id", ClientID).log().all().relaxedHTTPSValidation().when().body(requestBody).post()
				.then().log().all().extract().response();
		return response;

	}

	public Response getResponseGet(String baseURL,String ClientID) throws IOException {

		RestAssured.baseURI = baseURL;

		Response response = given().header("Authorization", "")
				.header("client_id", ClientID).log().all().relaxedHTTPSValidation().when().get().then().log().all()
				.extract().response();

		return response;

	}

}
