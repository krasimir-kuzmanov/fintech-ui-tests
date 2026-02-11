package com.example.fintech.ui.pages;

import com.codeborne.selenide.Selenide;

public abstract class BasePage<T extends BasePage<T>> {

  protected abstract String url();

  protected abstract T assertPageOpened();

  public T open() {
    Selenide.open(url());
    return assertPageOpened();
  }

  public T shouldBeOpened() {
    return assertPageOpened();
  }
}
