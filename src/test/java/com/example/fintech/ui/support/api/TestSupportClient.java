package com.example.fintech.ui.support.api;

import io.restassured.response.Response;

public class TestSupportClient {

  private static final String RESET_ENDPOINT = "/test/reset";

  public Response reset() {
    return ApiSupport.baseRequest()
        .when()
        .post(RESET_ENDPOINT);
  }
}
