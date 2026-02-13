package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.AuthResponse;
import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.model.UserResponse;

public class AuthSupportClient {

  private final AuthClient authClient = ApiSupport.client(AuthClient.class);

  public UserResponse register(RegisterRequest request) {
    return authClient.register(request);
  }

  public AuthResponse login(LoginRequest request) {
    return authClient.login(request);
  }

  public String loginAndGetToken(LoginRequest request) {
    AuthResponse response = login(request);
    String token = response.token();
    if (token == null || token.isBlank()) {
      throw new IllegalStateException("Login response token should not be blank");
    }
    return token;
  }

}
