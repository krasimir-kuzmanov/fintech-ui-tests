package com.example.fintech.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

  public static final String URL = "/login";

  private final SelenideElement username = $("[data-testid='login-username']");
  private final SelenideElement password = $("[data-testid='login-password']");
  private final SelenideElement submit = $("[data-testid='login-submit']");
  private final SelenideElement error = $("[data-testid='login-error']");
  private final SelenideElement goToRegister = $("[data-testid='go-to-register']");

  @Override
  protected String url() {
    return URL;
  }

  @Override
  protected LoginPage assertPageOpened() {
    username.shouldBe(visible);
    password.shouldBe(visible);
    submit.shouldBe(visible);
    goToRegister.shouldBe(visible);
    return this;
  }

  public void login(String username, String password) {
    setUsername(username);
    setPassword(password);
    submit();
  }

  public LoginPage setUsername(String value) {
    username.shouldBe(visible).setValue(value);
    return this;
  }

  public LoginPage setPassword(String value) {
    password.shouldBe(visible).setValue(value);
    return this;
  }

  public void submit() {
    submit.shouldBe(visible).click();
  }

  public LoginPage shouldShowError() {
    error.shouldBe(visible);
    return this;
  }

  public void goToRegister() {
    goToRegister.shouldBe(visible).click();
  }
}
