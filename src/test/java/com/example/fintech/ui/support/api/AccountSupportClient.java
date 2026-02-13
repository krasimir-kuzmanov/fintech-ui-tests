package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.AccountResponse;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountSupportClient {

  private final AccountClient accountClient = ApiSupport.client(AccountClient.class);

  public AccountResponse getAccount(String accountId, String token) {
    return accountClient.getAccount(accountId, ApiSupport.bearerToken(token));
  }

  public AccountResponse getAccountExpectOk(String accountId, String token) {
    AccountResponse response = getAccount(accountId, token);
    assertThat(response.accountId())
        .as("Get account should return requested account id")
        .isEqualTo(accountId);
    return response;
  }

  public AccountResponse fund(String accountId, BigDecimal amount, String token) {
    return accountClient.fundAccount(accountId, ApiSupport.bearerToken(token), Map.of("amount", amount));
  }

  public AccountResponse fundExpectOk(String accountId, BigDecimal amount, String token) {
    AccountResponse response = fund(accountId, amount, token);
    assertThat(response.accountId())
        .as("Fund account should return requested account id")
        .isEqualTo(accountId);
    return response;
  }
}
