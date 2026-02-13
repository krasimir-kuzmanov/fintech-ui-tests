package com.example.fintech.ui.tests.account;

import com.example.fintech.ui.pages.DashboardPage;
import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.support.api.AccountSupportClient;
import com.example.fintech.ui.support.api.AuthSupportClient;
import com.example.fintech.ui.support.model.AccountResponse;
import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.model.UserResponse;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FundBalanceUiTests extends BaseUiTest {

  private static final String FUND_AMOUNT = "75.25";
  private static final String INVALID_FUND_AMOUNT = "-10";

  private final AccountSupportClient accountSupportClient = new AccountSupportClient();
  private final AuthSupportClient authSupportClient = new AuthSupportClient();

  @Test
  void shouldFundAccountAndIncreaseBalance() {
    // given
    RegisterRequest user = UiTestDataFactory.userWithPrefix("ui_fund");
    UserResponse registerResponse = authSupportClient.register(user);

    // and
    String accountId = registerResponse.id();
    assertThat(accountId).isNotBlank();

    // when
    new LoginPage()
        .open()
        .login(user.username(), user.password());

    // and
    DashboardPage dashboardPage = new DashboardPage().shouldBeOpened();
    dashboardPage.fund(FUND_AMOUNT);

    // then
    dashboardPage.shouldHaveBalance(FundBalanceUiTests.FUND_AMOUNT);

    // when
    String token = authSupportClient.loginAndGetToken(new LoginRequest(user.username(), user.password()));
    BigDecimal apiBalance = fetchApiBalance(accountId, token);

    // then
    assertThat(apiBalance)
        .as("API balance should equal funded amount")
        .isEqualByComparingTo(FUND_AMOUNT);
  }

  @Test
  void shouldShowFundErrorWhenAmountIsInvalid() {
    // given
    RegisterRequest user = UiTestDataFactory.userWithPrefix("ui_fund_invalid");
    UserResponse registerResponse = authSupportClient.register(user);

    // and
    String accountId = registerResponse.id();
    String token = authSupportClient.loginAndGetToken(new LoginRequest(user.username(), user.password()));
    BigDecimal balanceBefore = fetchApiBalance(accountId, token);

    // when
    new LoginPage()
        .open()
        .login(user.username(), user.password());

    // and
    DashboardPage dashboardPage = new DashboardPage().shouldBeOpened();
    dashboardPage.fund(INVALID_FUND_AMOUNT);

    // then
    dashboardPage.shouldShowFundError();

    // when
    BigDecimal balanceAfter = fetchApiBalance(accountId, token);

    // then
    assertThat(balanceAfter)
        .as("Balance should stay unchanged after invalid funding attempt")
        .isEqualByComparingTo(balanceBefore);
  }

  private BigDecimal fetchApiBalance(String accountId, String token) {
    AccountResponse accountResponse = accountSupportClient.getAccount(accountId, token);
    return accountResponse.balance();
  }
}
