package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.TransactionResponse;

import java.util.List;

public class TransactionSupportClient {

  private final TransactionClient transactionClient = ApiSupport.client(TransactionClient.class);

  public List<TransactionResponse> getTransactions(String accountId, String token) {
    return transactionClient.getTransactions(accountId, ApiSupport.bearerToken(token));
  }
}
