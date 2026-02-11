package com.example.fintech.ui.support.api;

import com.example.fintech.ui.config.SelenideConfig;
import com.example.fintech.ui.support.testdata.HttpConstants;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public final class ApiSupport {

  private ApiSupport() {
    // utility class
  }

  public static RequestSpecification baseRequest() {
    return RestAssured.given()
        .baseUri(SelenideConfig.apiBaseUrl())
        .contentType(JSON)
        .accept(JSON);
  }

  public static RequestSpecification authRequest(String token) {
    return baseRequest()
        .header(HttpConstants.AUTH_HEADER, HttpConstants.BEARER_PREFIX + token);
  }
}
