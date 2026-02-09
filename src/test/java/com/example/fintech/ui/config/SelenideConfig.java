package com.example.fintech.ui.config;

import com.codeborne.selenide.Configuration;

import java.io.InputStream;
import java.util.Properties;

public final class SelenideConfig {

  private static final String CONFIG_FILE = "application.properties";
  private static final String CONFIG_UI_BASE_URL = "ui.baseUrl";
  private static final String CONFIG_API_BASE_URL = "api.baseUrl";
  private static final String CONFIG_TIMEOUT_MS = "selenide.timeoutMs";

  private static final int DEFAULT_TIMEOUT_MS = 10_000;

  private static final Properties PROPERTIES = loadProperties();
  private static boolean initialized = false;

  private SelenideConfig() {
    // utility class
  }

  public static synchronized void configure() {
    if (initialized) {
      return;
    }

    Configuration.browser = "chrome";
    Configuration.baseUrl = uiBaseUrl();
    Configuration.timeout = timeoutMs();

    Configuration.headless = false;

    Configuration.savePageSource = false;
    Configuration.screenshots = true;

    initialized = true;
  }

  public static String apiBaseUrl() {
    return getRequired(CONFIG_API_BASE_URL);
  }

  public static String uiBaseUrl() {
    return getRequired(CONFIG_UI_BASE_URL);
  }

  public static int timeoutMs() {
    String value = getOrDefault(CONFIG_TIMEOUT_MS, String.valueOf(DEFAULT_TIMEOUT_MS));

    try {
      return Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
      return DEFAULT_TIMEOUT_MS;
    }
  }

  private static String getRequired(String key) {
    String value = getOrDefault(key, null);

    if (value == null || value.isBlank()) {
      throw new RuntimeException("Property '" + key + "' is not defined");
    }

    return value;
  }

  private static String getOrDefault(String key, String defaultValue) {
    String sysProp = System.getProperty(key);

    if (sysProp != null && !sysProp.isBlank()) {
      return sysProp;
    }

    String value = PROPERTIES.getProperty(key, defaultValue);

    return (value == null || value.isBlank()) ? defaultValue : value;
  }

  private static Properties loadProperties() {
    Properties properties = new Properties();

    try (InputStream input =
             SelenideConfig.class
                 .getClassLoader()
                 .getResourceAsStream(CONFIG_FILE)) {

      if (input == null) {
        throw new RuntimeException("Could not find " + CONFIG_FILE + " on classpath");
      }

      properties.load(input);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load test configuration", e);
    }

    return properties;
  }
}
