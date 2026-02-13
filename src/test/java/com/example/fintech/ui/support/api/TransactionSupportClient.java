package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.TransactionResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionSupportClient {

  private final TransactionClient transactionClient = ApiSupport.client(TransactionClient.class);

  public List<TransactionResponse> getTransactions(String accountId, String token) {
    return transactionClient.getTransactions(accountId, ApiSupport.bearerToken(token));
  }

  public List<TransactionResponse> getTransactionsExpectOk(String accountId, String token) {
    List<TransactionResponse> response = getTransactions(accountId, token);
    assertThat(response)
        .as("Get transactions should return a non-null list")
        .isNotNull();
    return response;
  }
}
