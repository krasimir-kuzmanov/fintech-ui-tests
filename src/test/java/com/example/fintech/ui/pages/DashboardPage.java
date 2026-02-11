package com.example.fintech.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage extends BasePage<DashboardPage> {

  public static final String URL = "/dashboard";

  private final SelenideElement logout = $("[data-testid='logout-button']");
  private final SelenideElement balanceSection = $("[data-testid='balance-section']");
  private final SelenideElement balanceValue = $("[data-testid='balance-value']");
  private final SelenideElement fundAmount = $("[data-testid='fund-amount']");
  private final SelenideElement fundSubmit = $("[data-testid='fund-submit']");
  private final SelenideElement fundError = $("[data-testid='fund-error']");
  private final SelenideElement paymentToAccount = $("[data-testid='payment-to-account']");
  private final SelenideElement paymentAmount = $("[data-testid='payment-amount']");
  private final SelenideElement paymentSubmit = $("[data-testid='payment-submit']");
  private final SelenideElement paymentSuccess = $("[data-testid='payment-success']");
  private final SelenideElement transactionsSection = $("[data-testid='transactions-section']");
  private final ElementsCollection transactionItems = $$("[data-testid='transaction-item']");

  @Override
  protected String url() {
    return URL;
  }

  @Override
  protected DashboardPage assertPageOpened() {
    logout.shouldBe(visible);
    balanceSection.shouldBe(visible);
    transactionsSection.shouldBe(visible);
    return this;
  }

  public DashboardPage shouldHaveBalance(String expectedBalance) {
    balanceValue.shouldBe(visible).shouldHave(exactText(expectedBalance));
    return this;
  }

  public void fund(String value) {
    setFundAmount(value);
    submitFund();
  }

  public DashboardPage shouldShowFundError() {
    fundError.shouldBe(visible);
    return this;
  }

  public DashboardPage setFundAmount(String value) {
    fundAmount.shouldBe(visible).setValue(value);
    return this;
  }

  public void submitFund() {
    fundSubmit.shouldBe(visible).click();
  }

  public void makePayment(String toAccount, String amount) {
    setPaymentToAccount(toAccount);
    setPaymentAmount(amount);
    submitPayment();
  }

  public DashboardPage shouldShowPaymentSuccess() {
    paymentSuccess.shouldBe(visible);
    return this;
  }

  public DashboardPage setPaymentToAccount(String value) {
    paymentToAccount.shouldBe(visible).setValue(value);
    return this;
  }

  public DashboardPage setPaymentAmount(String value) {
    paymentAmount.shouldBe(visible).setValue(value);
    return this;
  }

  public void submitPayment() {
    paymentSubmit.shouldBe(visible).click();
  }

  public int transactionItemsCount() {
    return transactionItems.size();
  }

  public DashboardPage shouldHaveTransactionCountGreaterThan(int count) {
    transactionItems.shouldHave(sizeGreaterThan(count));
    return this;
  }

  public Set<String> uiTransactionIds() {
    Set<String> transactionIds = new LinkedHashSet<>();
    for (SelenideElement transactionItem : transactionItems) {
      transactionIds.add(transactionItem.getAttribute("data-txid"));
    }
    return transactionIds;
  }

  public void logout() {
    logout.shouldBe(visible).click();
  }
}
