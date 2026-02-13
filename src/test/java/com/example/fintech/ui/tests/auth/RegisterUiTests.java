package com.example.fintech.ui.tests.auth;

import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.pages.RegisterPage;
import com.example.fintech.ui.support.api.TestSupportClient;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.model.UserResponse;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
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
    registerPage.shouldShowSuccess();
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
    UserResponse userResponse = testSupportClient.getUserByUsername(expectedUsername);
    String userId = userResponse.id();
    String username = userResponse.username();

    assertThat(userId).isNotBlank();
    assertThat(username).isEqualTo(expectedUsername);
  }
}
