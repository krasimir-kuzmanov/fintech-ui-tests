package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.testdata.HttpConstants;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class AuthSupportClient {

  private static final String REGISTER_ENDPOINT = "/auth/register";
  private static final String LOGIN_ENDPOINT = "/auth/login";

  public Response register(RegisterRequest request) {
    return ApiSupport.baseRequest()
        .body(request)
        .when()
        .post(REGISTER_ENDPOINT);
  }

  public Response registerExpectOkOrCreated(RegisterRequest request) {
    Response response = register(request);
    response.then().statusCode(anyOf(
        is(HttpConstants.STATUS_OK),
        is(HttpConstants.STATUS_CREATED)
    ));
    return response;
  }

  public Response login(LoginRequest request) {
    return ApiSupport.baseRequest()
        .body(request)
        .when()
        .post(LOGIN_ENDPOINT);
  }

  public Response loginExpectOk(LoginRequest request) {
    Response response = login(request);
    response.then().statusCode(HttpConstants.STATUS_OK);
    return response;
  }

  public String loginAndGetToken(LoginRequest request) {
    Response response = loginExpectOk(request);
    String token = response.jsonPath().getString("token");
    if (token == null || token.isBlank()) {
      throw new IllegalStateException("Login response token should not be blank");
    }
    return token;
  }

}
