package com.example.fintech.ui.tests;

import com.codeborne.selenide.Selenide;
import com.example.fintech.ui.config.SelenideConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseUiTest {

  @BeforeAll
  static void globalSetup() {
    SelenideConfig.configure();
  }

  @BeforeEach
  void cleanBrowserState() {
    Selenide.clearBrowserCookies();
    Selenide.clearBrowserLocalStorage();
  }
}
