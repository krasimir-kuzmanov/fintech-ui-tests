package com.example.fintech.ui.support.api;

import com.example.fintech.ui.support.testdata.HttpConstants;
import io.restassured.response.Response;

public class AccountSupportClient {

  private static final String ACCOUNT_BY_ID_ENDPOINT = "/account/{accountId}";

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
}
