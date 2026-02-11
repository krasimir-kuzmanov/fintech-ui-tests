package com.example.fintech.ui.tests.account;

import com.example.fintech.ui.pages.DashboardPage;
import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.support.api.AuthSupportClient;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static org.assertj.core.api.Assertions.assertThat;

class FundBalanceUiTests extends BaseUiTest {

  private static final String FUND_AMOUNT = "75.25";

  private final AuthSupportClient authSupportClient = new AuthSupportClient();

  @Test
  void shouldFundAccountAndIncreaseBalance() {
    // given
    RegisterRequest user = UiTestDataFactory.userWithPrefix("ui_fund");
    authSupportClient.registerExpectOkOrCreated(user);

    // when
    new LoginPage()
        .open()
        .login(user.username(), user.password());

    // when
    DashboardPage dashboardPage = new DashboardPage().shouldBeOpened();
    dashboardPage.fund(FUND_AMOUNT);

    // then
    dashboardPage.balanceValue().shouldHave(exactText(FUND_AMOUNT));
    String balanceAfterFunding = dashboardPage.balanceValueText();
    assertThat(balanceAfterFunding)
        .as("Balance should equal funded amount for a new account")
        .isEqualTo(FUND_AMOUNT);
  }
}
