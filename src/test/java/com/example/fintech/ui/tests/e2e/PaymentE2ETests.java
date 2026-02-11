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
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentE2ETests extends BaseUiTest {

  private static final String ALICE_PREFIX = "ui_payment_alice";
  private static final String BOB_PREFIX = "ui_payment_bob";

  private static final BigDecimal FUND_AMOUNT = new BigDecimal("100.00");

  private static final String PAYMENT_AMOUNT = "40.00";
  private static final BigDecimal PAYMENT_AMOUNT_VALUE = new BigDecimal(PAYMENT_AMOUNT);

  private static final String SECOND_PAYMENT_AMOUNT = "20.00";
  private static final BigDecimal SECOND_PAYMENT_AMOUNT_VALUE = new BigDecimal(SECOND_PAYMENT_AMOUNT);

  private static final BigDecimal EXPECTED_BALANCE_AFTER_TWO_PAYMENTS = new BigDecimal("40.00");

  private final AccountSupportClient accountSupportClient = new AccountSupportClient();
  private final AuthSupportClient authSupportClient = new AuthSupportClient();
  private final TransactionSupportClient transactionSupportClient = new TransactionSupportClient();

  @Test
  void shouldMakePaymentAndMatchUiTransactionWithApi() {
    // given
    RegisterRequest alice = UiTestDataFactory.userWithPrefix(ALICE_PREFIX);
    RegisterRequest bob = UiTestDataFactory.userWithPrefix(BOB_PREFIX);

    // and
    String aliceAccountId = registerAndGetAccountId(alice);
    String bobAccountId = registerAndGetAccountId(bob);
    String aliceToken = authSupportClient.loginAndGetToken(new LoginRequest(alice.username(), alice.password()));

    // and
    accountSupportClient.fundExpectOk(aliceAccountId, FUND_AMOUNT, aliceToken);
    Response transactionsBeforeResponse = transactionSupportClient.getTransactionsExpectOk(aliceAccountId, aliceToken);
    List<Map<String, Object>> apiTransactionsBefore = transactionsBeforeResponse.jsonPath().getList("$");

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
    Response transactionsAfterResponse = transactionSupportClient.getTransactionsExpectOk(aliceAccountId, aliceToken);
    List<Map<String, Object>> apiTransactionsAfter = transactionsAfterResponse.jsonPath().getList("$");

    // then
    assertApiTransactionsMatchUiCount(apiTransactionsAfter, uiCountAfter);

    // and
    Map<String, Object> newTransaction = findNewTransaction(apiTransactionsBefore, apiTransactionsAfter);
    String newTransactionId = String.valueOf(newTransaction.get("transactionId"));
    assertNewPaymentTransaction(newTransaction, aliceAccountId, bobAccountId);

    // and
    assertUiMatchesApiTransactions(dashboardPage, Set.of(newTransactionId), apiTransactionsAfter.size());
  }

  @Test
  void shouldHandleMultiplePaymentsAndKeepUiApiInSync() {
    // given
    RegisterRequest alice = UiTestDataFactory.userWithPrefix(ALICE_PREFIX);
    RegisterRequest bob = UiTestDataFactory.userWithPrefix(BOB_PREFIX);

    // and
    String aliceAccountId = registerAndGetAccountId(alice);
    String bobAccountId = registerAndGetAccountId(bob);
    String aliceToken = authSupportClient.loginAndGetToken(new LoginRequest(alice.username(), alice.password()));

    // and
    accountSupportClient.fundExpectOk(aliceAccountId, FUND_AMOUNT, aliceToken);
    List<Map<String, Object>> apiTransactionsBefore = transactionSupportClient
        .getTransactionsExpectOk(aliceAccountId, aliceToken)
        .jsonPath()
        .getList("$");

    // when
    new LoginPage()
        .open()
        .login(alice.username(), alice.password());

    // and
    DashboardPage dashboardPage = new DashboardPage().shouldBeOpened();
    int uiCountBefore = dashboardPage.transactionItemsCount();

    // and
    dashboardPage.makePayment(bobAccountId, PAYMENT_AMOUNT);
    dashboardPage.shouldShowPaymentSuccess();
    dashboardPage.makePayment(bobAccountId, SECOND_PAYMENT_AMOUNT);
    dashboardPage
        .shouldShowPaymentSuccess()
        .shouldHaveTransactionCountGreaterThan(uiCountBefore + 1);

    // when
    List<Map<String, Object>> apiTransactionsAfter = transactionSupportClient
        .getTransactionsExpectOk(aliceAccountId, aliceToken)
        .jsonPath()
        .getList("$");

    // then
    List<Map<String, Object>> newTransactions = findNewTransactions(apiTransactionsBefore, apiTransactionsAfter, 2);
    Set<String> newTransactionIds = extractTransactionIds(newTransactions);

    // and
    assertUiMatchesApiTransactions(dashboardPage, newTransactionIds, apiTransactionsAfter.size());

    // and
    assertNewTransactionsForAliceToBob(newTransactions, aliceAccountId, bobAccountId);

    // and
    assertFinalBalance(aliceAccountId, aliceToken);
  }

  private String registerAndGetAccountId(RegisterRequest request) {
    Response registerResponse = authSupportClient.registerExpectOkOrCreated(request);
    String accountId = registerResponse.jsonPath().getString("id");
    assertThat(accountId).isNotBlank();

    return accountId;
  }

  private Map<String, Object> findNewTransaction(List<Map<String, Object>> before, List<Map<String, Object>> after) {
    return findNewTransactions(before, after, 1).getFirst();
  }

  private List<Map<String, Object>> findNewTransactions(List<Map<String, Object>> before, List<Map<String, Object>> after, int expectedCount) {
    Set<String> beforeIds = extractTransactionIds(before);
    List<Map<String, Object>> newTransactions = after.stream()
        .filter(transaction -> !beforeIds.contains(String.valueOf(transaction.get("transactionId"))))
        .toList();

    assertThat(newTransactions)
        .as("Unexpected number of new transactions after payment actions")
        .hasSize(expectedCount);

    return newTransactions;
  }

  private Set<String> extractTransactionIds(List<Map<String, Object>> transactions) {
    return transactions.stream()
        .map(transaction -> String.valueOf(transaction.get("transactionId")))
        .collect(Collectors.toSet());
  }

  private void assertNewPaymentTransaction(Map<String, Object> newTransaction, String expectedFromAccountId, String expectedToAccountId) {
    assertThat(new BigDecimal(String.valueOf(newTransaction.get("amount"))))
        .as("New API transaction amount should match payment amount")
        .isEqualByComparingTo(PaymentE2ETests.PAYMENT_AMOUNT_VALUE);

    assertThat(String.valueOf(newTransaction.get("fromAccountId")))
        .as("New API transaction should be a debit from Alice")
        .isEqualTo(expectedFromAccountId);

    assertThat(String.valueOf(newTransaction.get("toAccountId")))
        .as("New API transaction should be credited to Bob")
        .isEqualTo(expectedToAccountId);
  }

  private void assertNewTransactionsForAliceToBob(List<Map<String, Object>> newTransactions, String aliceAccountId, String bobAccountId) {
    List<BigDecimal> newTransactionAmounts = newTransactions.stream()
        .map(transaction -> new BigDecimal(String.valueOf(transaction.get("amount"))))
        .toList();

    assertThat(newTransactionAmounts).hasSize(2);
    assertThat(newTransactionAmounts).anySatisfy(amount -> assertThat(amount).isEqualByComparingTo(PAYMENT_AMOUNT_VALUE));
    assertThat(newTransactionAmounts).anySatisfy(amount -> assertThat(amount).isEqualByComparingTo(SECOND_PAYMENT_AMOUNT_VALUE));

    assertThat(newTransactions)
        .extracting(transaction -> String.valueOf(transaction.get("fromAccountId")))
        .containsOnly(aliceAccountId);

    assertThat(newTransactions)
        .extracting(transaction -> String.valueOf(transaction.get("toAccountId")))
        .containsOnly(bobAccountId);
  }

  private void assertApiTransactionsMatchUiCount(List<Map<String, Object>> apiTransactions, int uiCount) {
    assertThat(apiTransactions)
        .as("API should return at least one transaction")
        .isNotEmpty();

    assertThat(apiTransactions.size())
        .as("API transaction count should match UI count")
        .isEqualTo(uiCount);
  }

  private void assertUiMatchesApiTransactions(DashboardPage dashboardPage, Set<String> expectedTransactionIds, int expectedCount) {
    assertThat(dashboardPage.uiTransactionIds())
        .as("UI must display expected transaction ids")
        .containsAll(expectedTransactionIds);

    assertThat(dashboardPage.transactionItemsCount())
        .as("UI transaction count should match API transaction count")
        .isEqualTo(expectedCount);
  }

  private void assertFinalBalance(String accountId, String token) {
    BigDecimal finalBalance = accountSupportClient
        .getAccountExpectOk(accountId, token)
        .jsonPath()
        .getObject("balance", BigDecimal.class);

    assertThat(finalBalance)
        .as("Final Alice balance should match cumulative payments")
        .isEqualByComparingTo(PaymentE2ETests.EXPECTED_BALANCE_AFTER_TWO_PAYMENTS);
  }

}
