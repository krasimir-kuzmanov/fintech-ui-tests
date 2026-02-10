package com.example.fintech.ui.support.testdata;

import com.example.fintech.ui.support.model.request.RegisterRequest;

import java.util.UUID;

public final class UiTestDataFactory {

  public static final String DEFAULT_PASSWORD = "password";

  private UiTestDataFactory() {
    // utility class
  }

  public static RegisterRequest userWithPrefix(String prefix) {
    String username = prefix + "_" + UUID.randomUUID();
    return new RegisterRequest(username, DEFAULT_PASSWORD);
  }
}
