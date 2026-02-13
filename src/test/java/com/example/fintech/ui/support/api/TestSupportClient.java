package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.UserResponse;

public class TestSupportClient {

  private final TestClient testClient = ApiSupport.client(TestClient.class);

  public void reset() {
    testClient.reset();
  }

  public UserResponse getUserByUsername(String username) {
    return testClient.getUserByUsername(username);
  }
}
