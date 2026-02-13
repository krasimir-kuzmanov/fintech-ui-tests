package com.example.fintech.ui.support.api;

import com.example.fintech.ui.config.SelenideConfig;
import com.example.fintech.ui.support.testdata.HttpConstants;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public final class ApiSupport {

  private static final Feign.Builder FEIGN_BUILDER = Feign.builder()
      .encoder(new JacksonEncoder())
      .decoder(new JacksonDecoder());

  private ApiSupport() {
    // utility class
  }

  public static <T> T client(Class<T> apiType) {
    return FEIGN_BUILDER.target(apiType, SelenideConfig.apiBaseUrl());
  }

  public static String bearerToken(String token) {
    return HttpConstants.BEARER_PREFIX + token;
  }
}
