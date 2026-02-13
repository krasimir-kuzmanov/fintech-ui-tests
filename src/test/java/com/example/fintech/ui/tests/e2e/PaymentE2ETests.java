package com.example.fintech.ui.tests.e2e;

import com.example.fintech.ui.pages.DashboardPage;
import com.example.fintech.ui.pages.LoginPage;
import com.example.fintech.ui.support.api.AccountSupportClient;
import com.example.fintech.ui.support.api.AuthSupportClient;
import com.example.fintech.ui.support.api.TransactionSupportClient;
import com.example.fintech.ui.support.model.AccountResponse;
import com.example.fintech.ui.support.model.LoginRequest;
import com.example.fintech.ui.support.model.RegisterRequest;
import com.example.fintech.ui.support.model.TransactionResponse;
import com.example.fintech.ui.support.model.UserResponse;
import com.example.fintech.ui.support.testdata.UiTestDataFactory;
import com.example.fintech.ui.tests.BaseUiTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
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
  private static final String TOO_LARGE_PAYMENT_AMOUNT = "150.00";

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
    accountSupportClient.fund(aliceAccountId, FUND_AMOUNT, aliceToken);
    List<TransactionResponse> apiTransactionsBefore = transactionSupportClient.getTransactions(aliceAccountId, aliceToken);

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
    dashboardPage.shouldHaveTransactionCountGreaterThan(uiCountBefore);

    // then
    int uiCountAfter = dashboardPage.transactionItemsCount();
    assertThat(uiCountAfter)
        .as("UI transaction count should increase after payment")
        .isGreaterThan(uiCountBefore);

    // when
    List<TransactionResponse> apiTransactionsAfter = transactionSupportClient.getTransactions(aliceAccountId, aliceToken);

    // then
    assertApiTransactionsMatchUiCount(apiTransactionsAfter, uiCountAfter);

    // and
    TransactionResponse newTransaction = findNewTransaction(apiTransactionsBefore, apiTransactionsAfter);
    String newTransactionId = newTransaction.transactionId();
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
    accountSupportClient.fund(aliceAccountId, FUND_AMOUNT, aliceToken);
    List<TransactionResponse> apiTransactionsBefore = transactionSupportClient.getTransactions(aliceAccountId, aliceToken);

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
    dashboardPage.shouldShowPaymentSuccess();
    dashboardPage.shouldHaveTransactionCountGreaterThan(uiCountBefore + 1);

    // when
    List<TransactionResponse> apiTransactionsAfter = transactionSupportClient.getTransactions(aliceAccountId, aliceToken);

    // then
    List<TransactionResponse> newTransactions = findNewTransactions(apiTransactionsBefore, apiTransactionsAfter, 2);
    Set<String> newTransactionIds = extractTransactionIds(newTransactions);

    // and
    assertUiMatchesApiTransactions(dashboardPage, newTransactionIds, apiTransactionsAfter.size());

    // and
    assertNewTransactionsForAliceToBob(newTransactions, aliceAccountId, bobAccountId);

    // and
    assertFinalBalance(aliceAccountId, aliceToken);
  }

  @Test
  void shouldRejectPaymentWhenInsufficientFundsAndKeepUiApiInSync() {
    // given
    RegisterRequest alice = UiTestDataFactory.userWithPrefix(ALICE_PREFIX);
    RegisterRequest bob = UiTestDataFactory.userWithPrefix(BOB_PREFIX);

    // and
    String aliceAccountId = registerAndGetAccountId(alice);
    String bobAccountId = registerAndGetAccountId(bob);
    String aliceToken = authSupportClient.loginAndGetToken(new LoginRequest(alice.username(), alice.password()));
    accountSupportClient.fund(aliceAccountId, FUND_AMOUNT, aliceToken);

    // and
    BigDecimal balanceBefore = accountSupportClient.getAccount(aliceAccountId, aliceToken).balance();
    List<TransactionResponse> apiTransactionsBefore = transactionSupportClient.getTransactions(aliceAccountId, aliceToken);

    // when
    new LoginPage()
        .open()
        .login(alice.username(), alice.password());

    // and
    DashboardPage dashboardPage = new DashboardPage().shouldBeOpened();
    int uiCountBefore = dashboardPage.transactionItemsCount();
    dashboardPage.makePayment(bobAccountId, TOO_LARGE_PAYMENT_AMOUNT);

    // then
    dashboardPage.shouldShowPaymentError();
    assertThat(dashboardPage.transactionItemsCount())
        .as("UI transaction count should not change after rejected payment")
        .isEqualTo(uiCountBefore);

    // when
    BigDecimal balanceAfter = accountSupportClient.getAccount(aliceAccountId, aliceToken).balance();
    List<TransactionResponse> apiTransactionsAfter = transactionSupportClient.getTransactions(aliceAccountId, aliceToken);

    // then
    assertThat(balanceAfter)
        .as("Balance should remain unchanged after rejected payment")
        .isEqualByComparingTo(balanceBefore);

    assertThat(apiTransactionsAfter)
        .as("Transactions should remain unchanged after rejected payment")
        .hasSize(apiTransactionsBefore.size());

    assertThat(extractTransactionIds(apiTransactionsAfter))
        .as("Transaction ids should remain unchanged after rejected payment")
        .containsExactlyInAnyOrderElementsOf(extractTransactionIds(apiTransactionsBefore));
  }

  private String registerAndGetAccountId(RegisterRequest request) {
    UserResponse registerResponse = authSupportClient.register(request);
    String accountId = registerResponse.id();
    assertThat(accountId).isNotBlank();

    return accountId;
  }

  private TransactionResponse findNewTransaction(List<TransactionResponse> before, List<TransactionResponse> after) {
    return findNewTransactions(before, after, 1).getFirst();
  }

  private List<TransactionResponse> findNewTransactions(List<TransactionResponse> before, List<TransactionResponse> after, int expectedCount) {
    Set<String> beforeIds = extractTransactionIds(before);
    List<TransactionResponse> newTransactions = after.stream()
        .filter(transaction -> !beforeIds.contains(transaction.transactionId()))
        .toList();

    assertThat(newTransactions)
        .as("Unexpected number of new transactions after payment actions")
        .hasSize(expectedCount);

    return newTransactions;
  }

  private Set<String> extractTransactionIds(List<TransactionResponse> transactions) {
    return transactions.stream()
        .map(TransactionResponse::transactionId)
        .collect(Collectors.toSet());
  }

  private void assertNewPaymentTransaction(TransactionResponse newTransaction, String expectedFromAccountId, String expectedToAccountId) {
    assertThat(newTransaction.amount())
        .as("New API transaction amount should match payment amount")
        .isEqualByComparingTo(PaymentE2ETests.PAYMENT_AMOUNT_VALUE);

    assertThat(newTransaction.fromAccountId())
        .as("New API transaction should be a debit from Alice")
        .isEqualTo(expectedFromAccountId);

    assertThat(newTransaction.toAccountId())
        .as("New API transaction should be credited to Bob")
        .isEqualTo(expectedToAccountId);
  }

  private void assertNewTransactionsForAliceToBob(List<TransactionResponse> newTransactions, String aliceAccountId, String bobAccountId) {
    List<BigDecimal> newTransactionAmounts = newTransactions.stream()
        .map(TransactionResponse::amount)
        .toList();

    assertThat(newTransactionAmounts).hasSize(2);
    assertThat(newTransactionAmounts).anySatisfy(amount -> assertThat(amount).isEqualByComparingTo(PAYMENT_AMOUNT_VALUE));
    assertThat(newTransactionAmounts).anySatisfy(amount -> assertThat(amount).isEqualByComparingTo(SECOND_PAYMENT_AMOUNT_VALUE));

    assertThat(newTransactions)
        .extracting(TransactionResponse::fromAccountId)
        .containsOnly(aliceAccountId);

    assertThat(newTransactions)
        .extracting(TransactionResponse::toAccountId)
        .containsOnly(bobAccountId);
  }

  private void assertApiTransactionsMatchUiCount(List<TransactionResponse> apiTransactions, int uiCount) {
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
    AccountResponse accountResponse = accountSupportClient.getAccount(accountId, token);
    BigDecimal finalBalance = accountResponse.balance();

    assertThat(finalBalance)
        .as("Final Alice balance should match cumulative payments")
        .isEqualByComparingTo(PaymentE2ETests.EXPECTED_BALANCE_AFTER_TWO_PAYMENTS);
  }

}
