package com.example.fintech.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

  public static final String URL = "/register";

  private final SelenideElement page = $("[data-testid='register-page']");
  private final SelenideElement form = $("[data-testid='register-form']");
  private final SelenideElement username = $("[data-testid='register-username']");
  private final SelenideElement password = $("[data-testid='register-password']");
  private final SelenideElement submit = $("[data-testid='register-submit']");
  private final SelenideElement success = $("[data-testid='register-success']");
  private final SelenideElement backToLogin = $("[data-testid='go-to-login']");

  @Override
  protected String url() {
    return URL;
  }

  @Override
  protected RegisterPage assertPageOpened() {
    page.shouldBe(visible);
    form.shouldBe(visible);
    username.shouldBe(visible);
    password.shouldBe(visible);
    submit.shouldBe(visible);
    backToLogin.shouldBe(visible);
    return this;
  }

  public void register(String username, String password) {
    setUsername(username);
    setPassword(password);
    submit();
  }

  public RegisterPage setUsername(String value) {
    username.shouldBe(visible).setValue(value);
    return this;
  }

  public RegisterPage setPassword(String value) {
    password.shouldBe(visible).setValue(value);
    return this;
  }

  public void submit() {
    submit.shouldBe(visible).click();
  }

  public void goToLogin() {
    backToLogin.shouldBe(visible).click();
  }

  public RegisterPage shouldShowSuccess() {
    success.shouldBe(visible);
    return this;
  }
}
