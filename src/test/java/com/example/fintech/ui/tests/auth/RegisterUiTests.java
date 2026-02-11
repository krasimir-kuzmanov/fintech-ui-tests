package com.example.fintech.ui.tests.auth;

import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.pages.RegisterPage;
import com.example.fintech.ui.support.api.TestSupportClient;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterUiTests extends BaseUiTest {

  private final TestSupportClient testSupportClient = new TestSupportClient();

  @Test
  void shouldRegisterSuccessfullyAndShowSuccessMessage() {
    // given
    RegisterRequest user = UiTestDataFactory.userWithPrefix("ui_register");
    RegisterPage registerPage = new RegisterPage().open();

    // when
    registerPage.register(user.username(), user.password());

    // then
    assertThat(registerPage.isSuccessVisible()).isTrue();
    assertUserExistsWithUsername(user.username());
  }

  @Test
  void shouldNavigateBackToLoginFromRegisterPage() {
    // given
    RegisterPage registerPage = new RegisterPage().open();

    // when
    registerPage.goToLogin();

    // then
    new LoginPage().shouldBeOpened();
  }

  private void assertUserExistsWithUsername(String expectedUsername) {
    Response userResponse = testSupportClient.getUserByUsernameExpectOk(expectedUsername);
    JsonPath userJson = userResponse.jsonPath();

    String userId = userJson.getString("id");
    String username = userJson.getString("username");

    assertThat(userId).isNotBlank();
    assertThat(username).isEqualTo(expectedUsername);
  }
}
