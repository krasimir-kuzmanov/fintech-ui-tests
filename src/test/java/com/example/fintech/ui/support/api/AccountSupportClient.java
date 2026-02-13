package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.AccountResponse;

import java.math.BigDecimal;
import java.util.Map;

public class AccountSupportClient {

  private final AccountClient accountClient = ApiSupport.client(AccountClient.class);

  public AccountResponse getAccount(String accountId, String token) {
    return accountClient.getAccount(accountId, ApiSupport.bearerToken(token));
  }

  public AccountResponse fund(String accountId, BigDecimal amount, String token) {
    return accountClient.fundAccount(accountId, ApiSupport.bearerToken(token), Map.of("amount", amount));
  }
}
