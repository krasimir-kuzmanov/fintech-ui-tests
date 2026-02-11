package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.testdata.HttpConstants;
import io.restassured.response.Response;

public class TestSupportClient {

  private static final String RESET_ENDPOINT = "/test/reset";
  private static final String USER_BY_USERNAME_ENDPOINT = "/test/users/{username}";

  public Response reset() {
    return ApiSupport.baseRequest()
        .when()
        .post(RESET_ENDPOINT);
  }

  public Response getUserByUsername(String username) {
    return ApiSupport.baseRequest()
        .pathParam("username", username)
        .when()
        .get(USER_BY_USERNAME_ENDPOINT);
  }

  public Response getUserByUsernameExpectOk(String username) {
    Response response = getUserByUsername(username);
    response.then().statusCode(HttpConstants.STATUS_OK);
    return response;
  }
}
