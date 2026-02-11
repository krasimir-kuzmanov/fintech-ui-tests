package com.example.fintech.ui.tests.auth;

import com.example.fintech.ui.pages.DashboardPage;
import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.support.api.AuthSupportClient;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import org.junit.jupiter.api.Test;

class LoginUiTests extends BaseUiTest {

  private final AuthSupportClient authSupportClient = new AuthSupportClient();

  @Test
  void shouldLoginSuccessfullyAndOpenDashboard() {
    // given
    RegisterRequest user = UiTestDataFactory.userWithPrefix("ui_login");
    authSupportClient.registerExpectOkOrCreated(user);

    // when
    new LoginPage()
        .open()
        .login(user.username(), user.password());

    // then
    new DashboardPage().shouldBeOpened();
  }
}
