package com.example.fintech.ui.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.example.fintech.ui.config.SelenideConfig;
import com.example.fintech.ui.support.api.TestSupportClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseUiTest {

  private final TestSupportClient testSupportClient = new TestSupportClient();

  @BeforeAll
  static void globalSetup() {
    SelenideConfig.configure();
  }

  @BeforeEach
  void setup() {
    testSupportClient.reset();

    if (!WebDriverRunner.hasWebDriverStarted()) {
      return;
    }

    Selenide.clearBrowserCookies();
    Selenide.clearBrowserLocalStorage();
  }
}
