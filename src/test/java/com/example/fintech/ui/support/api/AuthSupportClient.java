package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.request.LoginRequest;
import com.example.fintech.ui.support.model.request.RegisterRequest;
import io.restassured.response.Response;

public class AuthSupportClient {

  private static final String REGISTER_ENDPOINT = "/auth/register";
  private static final String LOGIN_ENDPOINT = "/auth/login";
  private static final String LOGOUT_ENDPOINT = "/auth/logout";

  public Response register(RegisterRequest request) {
    return ApiSupport.baseRequest()
        .body(request)
        .when()
        .post(REGISTER_ENDPOINT);
  }

  public Response login(LoginRequest request) {
    return ApiSupport.baseRequest()
        .body(request)
        .when()
        .post(LOGIN_ENDPOINT);
  }

  public Response logout(String token) {
    return ApiSupport.authRequest(token)
        .when()
        .post(LOGOUT_ENDPOINT);
  }
}
