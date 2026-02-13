package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.AuthResponse;
import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.model.UserResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSupportClient {

  private final AuthClient authClient = ApiSupport.client(AuthClient.class);

  public UserResponse register(RegisterRequest request) {
    return authClient.register(request);
  }

  public UserResponse registerExpectOkOrCreated(RegisterRequest request) {
    UserResponse response = register(request);
    assertThat(response.id())
        .as("Register response should contain user id")
        .isNotBlank();
    return response;
  }

  public AuthResponse login(LoginRequest request) {
    return authClient.login(request);
  }

  public AuthResponse loginExpectOk(LoginRequest request) {
    AuthResponse response = login(request);
    assertThat(response.userId())
        .as("Login response should contain user id")
        .isNotBlank();
    return response;
  }

  public String loginAndGetToken(LoginRequest request) {
    AuthResponse response = loginExpectOk(request);
    String token = response.token();
    if (token == null || token.isBlank()) {
      throw new IllegalStateException("Login response token should not be blank");
    }
    return token;
  }

}
