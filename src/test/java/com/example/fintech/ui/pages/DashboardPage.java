package com.example.fintech.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage extends BasePage<DashboardPage> {

  private final SelenideElement logout = $("[data-testid='logout-button']");
  private final SelenideElement balanceSection = $("[data-testid='balance-section']");
  private final SelenideElement balanceValue = $("[data-testid='balance-value']");

  @Override
  protected String url() {
    return "/dashboard";
  }

  @Override
  protected DashboardPage assertPageOpened() {
    logout.shouldBe(visible);
    balanceSection.shouldBe(visible);
    return this;
  }

  public SelenideElement balanceSection() {
    return balanceSection;
  }

  public SelenideElement balanceValue() {
    return balanceValue;
  }

  public void logout() {
    logout.shouldBe(visible).click();
  }
}
