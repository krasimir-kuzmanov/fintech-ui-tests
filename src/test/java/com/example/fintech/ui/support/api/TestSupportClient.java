package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.UserResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSupportClient {

  private final TestClient testClient = ApiSupport.client(TestClient.class);

  public void reset() {
    testClient.reset();
  }

  public UserResponse getUserByUsername(String username) {
    return testClient.getUserByUsername(username);
  }

  public UserResponse getUserByUsernameExpectOk(String username) {
    UserResponse response = getUserByUsername(username);
    assertThat(response.id())
        .as("Get user by username should return user id")
        .isNotBlank();
    return response;
  }
}
