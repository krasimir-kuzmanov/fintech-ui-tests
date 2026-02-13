package com.example.fintech.ui.support.api.client;

import com.example.fintech.ui.support.model.UserResponse;
import feign.Param;
import feign.RequestLine;

public interface TestClient {

  @RequestLine("POST /test/reset")
  void reset();

  @RequestLine("GET /test/users/{username}")
  UserResponse getUserByUsername(@Param("username") String username);
}
