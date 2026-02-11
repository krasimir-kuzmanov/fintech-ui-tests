package com.example.fintech.ui.tests.e2e;

import com.example.fintech.ui.pages.DashboardPage;
import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.support.api.AccountSupportClient;
import com.example.fintech.ui.support.api.AuthSupportClient;
import com.example.fintech.ui.support.api.TransactionSupportClient;
import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentE2ETests extends BaseUiTest {

  private static final String ALICE_PREFIX = "ui_payment_alice";
  private static final String BOB_PREFIX = "ui_payment_bob";
  private static final BigDecimal FUND_AMOUNT = new BigDecimal("100.00");
  private static final String PAYMENT_AMOUNT = "40.00";

  private final AccountSupportClient accountSupportClient = new AccountSupportClient();
  private final AuthSupportClient authSupportClient = new AuthSupportClient();
  private final TransactionSupportClient transactionSupportClient = new TransactionSupportClient();

  @Test
  void shouldMakePaymentAndMatchUiLastTransactionAmountWithApi() {
    // given
    RegisterRequest alice = UiTestDataFactory.userWithPrefix(ALICE_PREFIX);
    RegisterRequest bob = UiTestDataFactory.userWithPrefix(BOB_PREFIX);

    // and
    String aliceAccountId = registerAndGetAccountId(alice);
    String bobAccountId = registerAndGetAccountId(bob);
    String aliceToken = loginAndReturnToken(alice);

    // and
    accountSupportClient.fundExpectOk(aliceAccountId, FUND_AMOUNT, aliceToken);

    // when
    new LoginPage()
        .open()
        .login(alice.username(), alice.password());

    // and
    DashboardPage dashboardPage = new DashboardPage().shouldBeOpened();
    int uiCountBefore = dashboardPage.transactionItemsCount();

    // and
    dashboardPage.makePayment(bobAccountId, PAYMENT_AMOUNT);
    dashboardPage
        .shouldShowPaymentSuccess()
        .shouldHaveTransactionCountGreaterThan(uiCountBefore);

    // then
    int uiCountAfter = dashboardPage.transactionItemsCount();
    assertThat(uiCountAfter)
        .as("UI transaction count should increase after payment")
        .isGreaterThan(uiCountBefore);

    // when
    Response transactionsResponse = transactionSupportClient.getTransactionsExpectOk(aliceAccountId, aliceToken);
    List<Map<String, Object>> apiTransactions = transactionsResponse.jsonPath().getList("$");

    // then
    assertThat(apiTransactions)
        .as("API should return at least one transaction")
        .isNotEmpty();
    assertThat(apiTransactions.size())
        .as("API transaction count should match UI count")
        .isEqualTo(uiCountAfter);

    // and
    int apiLastIndex = apiTransactions.size() - 1;
    String apiLastAmount = transactionsResponse.jsonPath().getString("[" + apiLastIndex + "].amount");
    String uiLastAmount = dashboardPage.lastTransactionAmountText();

    assertThat(new BigDecimal(uiLastAmount))
        .as("Last UI transaction amount should match last API transaction amount")
        .isEqualByComparingTo(new BigDecimal(apiLastAmount));
  }

  private String registerAndGetAccountId(RegisterRequest request) {
    Response registerResponse = authSupportClient.registerExpectOkOrCreated(request);
    String accountId = registerResponse.jsonPath().getString("id");
    assertThat(accountId).isNotBlank();
    return accountId;
  }

  private String loginAndReturnToken(RegisterRequest request) {
    LoginRequest loginRequest = new LoginRequest(request.username(), request.password());
    Response loginResponse = authSupportClient.loginExpectOk(loginRequest);
    String token = loginResponse.jsonPath().getString("token");
    assertThat(token).isNotBlank();
    return token;
  }
}
