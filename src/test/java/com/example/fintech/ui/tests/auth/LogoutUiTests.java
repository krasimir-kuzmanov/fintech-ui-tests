package com.example.fintech.ui.tests.auth;

import com.example.fintech.ui.pages.DashboardPage;
import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.support.api.AuthSupportClient;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

class LogoutUiTests extends BaseUiTest {

  private final AuthSupportClient authSupportClient = new AuthSupportClient();

  @Test
  void shouldLogoutAndRedirectToLogin() {
    // given
    RegisterRequest user = UiTestDataFactory.userWithPrefix("ui_logout");
    authSupportClient.registerExpectOkOrCreated(user);

    // when
    new LoginPage()
        .open()
        .login(user.username(), user.password());

    // and
    new DashboardPage()
        .shouldBeOpened()
        .logout();

    // then
    new LoginPage().shouldBeOpened();

    // when
    open(DashboardPage.URL);

    // then
    new LoginPage().shouldBeOpened();
  }
}
