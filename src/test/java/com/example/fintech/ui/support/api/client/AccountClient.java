package com.example.fintech.ui.support.api.client;

import com.example.fintech.ui.support.model.AccountResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.math.BigDecimal;
import java.util.Map;

public interface AccountClient {

  @RequestLine("GET /account/{accountId}")
  @Headers("Authorization: {authorization}")
  AccountResponse getAccount(
      @Param("accountId") String accountId,
      @Param("authorization") String authorization
  );

  @RequestLine("POST /account/{accountId}/fund")
  @Headers({
      "Authorization: {authorization}",
      "Content-Type: application/json"
  })
  AccountResponse fundAccount(
      @Param("accountId") String accountId,
      @Param("authorization") String authorization,
      Map<String, BigDecimal> body
  );
}
