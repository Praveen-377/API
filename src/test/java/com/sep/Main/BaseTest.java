package com.sep.Main;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
public class BaseTest {
		protected RequestSpecification request;
		String GitHUB_TOKEN = "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";

		public BaseTest() {
			RestAssured.baseURI = "https://api.github.com/";
			request = RestAssured.given().headers("Authorization", "Bearer " + GitHUB_TOKEN);

		}

	}
