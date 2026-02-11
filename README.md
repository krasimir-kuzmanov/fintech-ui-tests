# fintech-ui-tests

UI test suite for Fintech frontend flows using Selenide + JUnit 5.

## Tech Stack
- Java 21 (Gradle toolchain)
- Gradle Wrapper
- JUnit 5
- Selenide
- RestAssured (for test support APIs)

## Prerequisites
- JDK 21 installed
- SDKMAN available (project includes `.sdkmanrc`)
- No local Gradle install required (wrapper included)
- `fintech-backend` running (default: `http://localhost:8080`)
- `fintech-frontend` running (default: `http://localhost:5173`)

## Run
```bash
sdk env
./gradlew test
```
UI routes under test: `/login`, `/register`, `/dashboard`.
Tests assume Google Chrome is installed and available locally.

## Configuration
Loaded from `src/test/resources/application.properties`.

Required:
- `ui.baseUrl`
- `api.baseUrl`

Optional:
- `selenide.timeoutMs` (default `10000`)

You can override via JVM props, for example:
```bash
./gradlew test -Dui.baseUrl=http://localhost:5173 -Dapi.baseUrl=http://localhost:8080
```

## Project Structure
- `src/test/java/com/example/fintech/ui/config`
- `src/test/java/com/example/fintech/ui/pages`
- `src/test/java/com/example/fintech/ui/support/api`
- `src/test/java/com/example/fintech/ui/tests`

## Notes
- Browser configuration is centralized in `src/test/java/com/example/fintech/ui/config/SelenideConfig.java`.
- Base test setup is in `src/test/java/com/example/fintech/ui/tests/BaseUiTest.java`.
