package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.testdata.HttpConstants;
import io.restassured.response.Response;

public class TransactionSupportClient {

  private static final String TRANSACTIONS_BY_ACCOUNT_ENDPOINT = "/transaction/{accountId}";

  public Response getTransactions(String accountId, String token) {
    return ApiSupport.authRequest(token)
        .pathParam("accountId", accountId)
        .when()
        .get(TRANSACTIONS_BY_ACCOUNT_ENDPOINT);
  }

  public Response getTransactionsExpectOk(String accountId, String token) {
    Response response = getTransactions(accountId, token);
    response.then().statusCode(HttpConstants.STATUS_OK);
    return response;
  }
}
