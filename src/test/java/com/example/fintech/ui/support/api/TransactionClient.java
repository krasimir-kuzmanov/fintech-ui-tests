package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.model.TransactionResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

interface TransactionClient {

  @RequestLine("GET /transaction/{accountId}")
  @Headers("Authorization: {authorization}")
  List<TransactionResponse> getTransactions(
      @Param("accountId") String accountId,
      @Param("authorization") String authorization
  );
}
