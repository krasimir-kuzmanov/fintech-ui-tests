package com.example.fintech.ui.tests.account;

import com.example.fintech.ui.pages.DashboardPage;
import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.support.api.AccountSupportClient;
import com.example.fintech.ui.support.api.AuthSupportClient;
import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FundBalanceUiTests extends BaseUiTest {

  private static final String FUND_AMOUNT = "75.25";

  private final AccountSupportClient accountSupportClient = new AccountSupportClient();
  private final AuthSupportClient authSupportClient = new AuthSupportClient();

  @Test
  void shouldFundAccountAndIncreaseBalance() {
    // given
    RegisterRequest user = UiTestDataFactory.userWithPrefix("ui_fund");
    Response registerResponse = authSupportClient.registerExpectOkOrCreated(user);

    // and
    String accountId = registerResponse.jsonPath().getString("id");
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
    String token = loginAndReturnToken(user);
    BigDecimal apiBalance = fetchApiBalance(accountId, token);

    // then
    assertThat(apiBalance)
        .as("API balance should equal funded amount")
        .isEqualByComparingTo(FUND_AMOUNT);
  }

  private String loginAndReturnToken(RegisterRequest user) {
    LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
    Response loginResponse = authSupportClient.loginExpectOk(loginRequest);
    JsonPath loginJson = loginResponse.jsonPath();
    String accountId = loginJson.getString("userId");
    String token = loginJson.getString("token");

    assertThat(accountId).isNotBlank();
    assertThat(token).isNotBlank();

    return token;
  }

  private BigDecimal fetchApiBalance(String accountId, String token) {
    Response accountResponse = accountSupportClient.getAccountExpectOk(accountId, token);
    JsonPath accountJson = accountResponse.jsonPath();

    return accountJson.getObject("balance", BigDecimal.class);
  }
}
