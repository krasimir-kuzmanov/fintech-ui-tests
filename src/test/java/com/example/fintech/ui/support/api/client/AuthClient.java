package com.example.fintech.ui.support.api.client;

import com.example.fintech.ui.support.model.AuthResponse;
import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.model.UserResponse;
import feign.Headers;
import feign.RequestLine;

public interface AuthClient {

  @RequestLine("POST /auth/register")
  @Headers("Content-Type: application/json")
  UserResponse register(RegisterRequest request);

  @RequestLine("POST /auth/login")
  @Headers("Content-Type: application/json")
  AuthResponse login(LoginRequest request);
}
