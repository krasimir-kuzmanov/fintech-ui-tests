# fintech-ui-tests

[![UI Tests](https://github.com/krasimir-kuzmanov/fintech-ui-tests/actions/workflows/ui-tests.yml/badge.svg)](https://github.com/krasimir-kuzmanov/fintech-ui-tests/actions/workflows/ui-tests.yml)

UI test suite for Fintech frontend flows using Selenide + JUnit 5.

## Tech Stack
- Java 21 (Gradle toolchain)
- Gradle Wrapper
- JUnit 5
- Selenide
- OpenFeign + Jackson (for test support APIs and cross-checks)

## Requirements
- Java 21
- Node.js 18+ (recommended: 20)
- Google Chrome (latest stable)
- SDKMAN (recommended; project includes `.sdkmanrc`)

No local Gradle install is required (wrapper included).

## Quick Start
### 1) Start the Backend
```bash
cd ../fintech-backend
./gradlew bootRun
```

Backend default URL:
- `http://localhost:8080`

Health check:
- `http://localhost:8080/actuator/health`

### 2) Start the Frontend
```bash
cd ../fintech-frontend
npm install
npm run dev
```

Frontend default URL:
- `http://localhost:5173`

### 3) Run the UI Tests
From the `fintech-ui-tests` project root:

```bash
sdk env
./gradlew clean test
```

The tests:
- reset backend state before each test (`POST /test/reset`)
- launch Chrome via Selenide
- execute UI flows against `/login`, `/register`, `/dashboard`
- cross-check critical flows against backend APIs

### Test Report
After execution:
- `build/reports/tests/test/index.html`

## Configuration
Loaded from `src/test/resources/application.properties`.

Required:
- `ui.baseUrl`
- `api.baseUrl`

Optional:
- `selenide.timeoutMs` (default `10000`)
- `selenide.headless` (defaults to `false` locally, `true` in GitHub Actions)

You can override via JVM properties:
```bash
./gradlew test -Dui.baseUrl=http://localhost:5173 -Dapi.baseUrl=http://localhost:8080
```

## What the Suite Covers

### Authentication
- user registration (happy path)
- login (happy path)
- logout with route protection after logout

### Account & Balance
- fund account via UI
- balance reflects successful funding
- invalid funding amount shows UI error and preserves balance

### Payments (UI <-> API consistency)
- single payment flow
- multiple payments in one session
- exact transaction ID appears in UI (`data-txid`)
- UI transaction count matches backend API
- backend transaction integrity is validated

## Project Structure
- Selenide + JUnit 5
- Page Object pattern
- support API clients for setup and API assertions
- deterministic test isolation via `/test/reset`

## CI
GitHub Actions workflow:
- `.github/workflows/ui-tests.yml`

It runs on push to `main`, pull requests, and manual dispatch.

## Notes
- This suite is intentionally small and high-signal.
- Validation edge cases are primarily covered by `fintech-api-tests`.
- UI tests focus on user-visible behavior and system-level consistency.

## Goal
Demonstrate:
- clean UI automation architecture
- clear separation of concerns (UI vs API tests)
- robust end-to-end consistency validation
