# fintech-ui-tests

UI test suite for the Fintech project using Selenide and JUnit 5.

This project is designed to validate end-to-end user flows in `fintech-frontend` against the real `fintech-backend`.

**Tech Stack**
- Java: 21 (Gradle toolchain)
- Build: Gradle Wrapper
- Test Framework: JUnit 5
- UI Automation: Selenide
- API Helpers (optional in UI flows): RestAssured

## SDK
- Java SDK: JDK 21 (Gradle toolchain targets Java 21)

## Prerequisites
- JDK 21 installed
- SDKMAN available (project includes `.sdkmanrc`)
- No local Gradle install required (wrapper included)
- `fintech-backend` running (default: `http://localhost:8080`)
- `fintech-frontend` running (default: `http://localhost:5174`)

## Run Locally
From the project root:

```bash
sdk env
./gradlew test
```

Override config in CI or locally (optional):

```bash
./gradlew test -Dui.baseUrl=http://ci-host:5174 -Dapi.baseUrl=http://ci-host:8080 -Dselenide.timeoutMs=10000
```

## Runtime Configuration
Test configuration is loaded from `src/test/resources/application.properties`.

Required properties:
- `ui.baseUrl`
- `api.baseUrl`

Optional property:
- `selenide.timeoutMs` (defaults to `10000` if missing/invalid)

## Project Layout
- This is a test-only project (no `src/main`).
- Test sources: `src/test/java/com/example/fintech/ui`
- Main packages:
- `config` (Selenide and test configuration)
- `pages` (Page Objects)
- `support/api` (API helpers for test setup/cleanup)
- `tests` (UI test classes)
- Test resources: `src/test/resources`

## Common Gradle Tasks
- `./gradlew clean` - clean build outputs
- `./gradlew test` - run UI test suite
- `./gradlew build` - compile and run all checks

## Notes
- Browser configuration is centralized in `src/test/java/com/example/fintech/ui/config/SelenideConfig.java`.
- Base test setup is in `src/test/java/com/example/fintech/ui/tests/BaseUiTest.java`.
