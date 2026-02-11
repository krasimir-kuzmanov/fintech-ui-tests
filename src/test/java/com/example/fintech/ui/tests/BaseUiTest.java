package com.example.fintech.ui.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.example.fintech.ui.config.SelenideConfig;
import com.example.fintech.ui.support.api.TestSupportClient;
import com.example.fintech.ui.support.testdata.HttpConstants;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseUiTest {

  private final TestSupportClient testSupportClient = new TestSupportClient();

  @BeforeAll
  static void globalSetup() {
    SelenideConfig.configure();
  }

  @BeforeEach
  void setup() {
    Response response = testSupportClient.reset();
    assertThat(response.statusCode())
        .as("Reset endpoint should succeed")
        .isIn(HttpConstants.STATUS_OK, HttpConstants.STATUS_NO_CONTENT);

    if (!WebDriverRunner.hasWebDriverStarted()) {
      return;
    }

    Selenide.clearBrowserCookies();
    Selenide.clearBrowserLocalStorage();
  }
}
