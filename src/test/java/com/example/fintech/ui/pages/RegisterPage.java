package com.example.fintech.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

  private final SelenideElement page = $("div[data-testid='register-page']");
  private final SelenideElement form = $("form[data-testid='register-form']");
  private final SelenideElement username = $("input[data-testid='register-username']");
  private final SelenideElement password = $("input[data-testid='register-password']");
  private final SelenideElement submit = $("button[data-testid='register-submit']");
  private final SelenideElement error = $("div[data-testid='register-error']");
  private final SelenideElement success = $("div[data-testid='register-success']");
  private final SelenideElement backToLogin = $("button[data-testid='go-to-login']");

  @Override
  protected String url() {
    return "/register";
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

  public SelenideElement error() {
    return error;
  }

  public SelenideElement success() {
    return success;
  }
}
