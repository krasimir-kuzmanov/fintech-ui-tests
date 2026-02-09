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
- `fintech-backend` running on `http://localhost:8080`
- `fintech-frontend` running on `http://localhost:5173`

## Run Locally
From the project root:

```bash
sdk env
./gradlew test
```

## Target Applications
- Frontend under test: `http://localhost:5173`
- Backend used by frontend: `http://localhost:8080`

Before running UI tests, start:
- `fintech-backend`
- `fintech-frontend` (with `VITE_API_BASE_URL=http://localhost:8080`)

## Project Layout
- Test sources: `src/test/java`
- Test resources: `src/test/resources`

## Common Gradle Tasks
- `./gradlew clean` - clean build outputs
- `./gradlew test` - run UI test suite
- `./gradlew build` - compile and run all checks

## Notes
- This repository currently contains the UI test project scaffold and dependencies.
- Initial Selenide test coverage can be added for login, registration, dashboard balance, funding, payment, and logout flows.
