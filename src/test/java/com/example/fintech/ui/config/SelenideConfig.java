package com.example.fintech.ui.config;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.InputStream;
import java.util.Properties;

public final class SelenideConfig {

  private static final String CONFIG_FILE = "application.properties";
  private static final String CONFIG_UI_BASE_URL = "ui.baseUrl";
  private static final String CONFIG_API_BASE_URL = "api.baseUrl";
  private static final String CONFIG_TIMEOUT_MS = "selenide.timeoutMs";
  private static final String CONFIG_HEADLESS = "selenide.headless";

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

    Configuration.headless = headless();
    Configuration.browserCapabilities = chromeOptions();

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

  public static boolean headless() {
    String defaultValue = isCiEnvironment() ? "true" : "false";
    String value = getOrDefault(CONFIG_HEADLESS, defaultValue);
    return Boolean.parseBoolean(value);
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

  private static ChromeOptions chromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--window-size=1920,1080");
    options.addArguments("--disable-gpu");

    if (isCiEnvironment()) {
      options.addArguments("--no-sandbox");
      options.addArguments("--disable-dev-shm-usage");
    }

    return options;
  }

  private static boolean isCiEnvironment() {
    return "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
  }
}
