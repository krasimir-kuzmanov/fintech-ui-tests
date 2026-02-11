package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.testdata.HttpConstants;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.Map;

public class AccountSupportClient {

  private static final String ACCOUNT_BY_ID_ENDPOINT = "/account/{accountId}";
  private static final String FUND_ACCOUNT_ENDPOINT = "/account/{accountId}/fund";

  public Response getAccount(String accountId, String token) {
    return ApiSupport.authRequest(token)
        .pathParam("accountId", accountId)
        .when()
        .get(ACCOUNT_BY_ID_ENDPOINT);
  }

  public Response getAccountExpectOk(String accountId, String token) {
    Response response = getAccount(accountId, token);
    response.then().statusCode(HttpConstants.STATUS_OK);
    return response;
  }

  public Response fund(String accountId, BigDecimal amount, String token) {
    return ApiSupport.authRequest(token)
        .pathParam("accountId", accountId)
        .body(Map.of("amount", amount))
        .when()
        .post(FUND_ACCOUNT_ENDPOINT);
  }

  public Response fundExpectOk(String accountId, BigDecimal amount, String token) {
    Response response = fund(accountId, amount, token);
    response.then().statusCode(HttpConstants.STATUS_OK);
    return response;
  }
}
