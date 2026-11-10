# Contributing to SpendWise

First of all, thank you for considering contributing to **SpendWise**! 🎉

SpendWise is a backend-first personal finance and expense management application built with **Java, Spring Boot, PostgreSQL, and JWT-based authentication**.

Contributions are welcome, whether they involve fixing bugs, improving documentation, adding tests, refining existing functionality, or proposing new features.

---

## Table of Contents

- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
  - [Prerequisites](#prerequisites)
  - [Clone the Repository](#clone-the-repository)
  - [Environment Variables & Database Setup](#environment-variables--database-setup)
  - [Run the Application](#run-the-application)
- [Creating a Branch](#creating-a-branch)
- [Making Changes](#making-changes)
- [Testing](#testing)
- [Commit Guidelines](#commit-guidelines)
- [Pull Requests](#pull-requests)
- [Reporting Bugs](#reporting-bugs)
- [Suggesting Features](#suggesting-features)
- [Security](#security)
- [Code of Conduct](#code-of-conduct)
- [Questions](#questions)

---

## Getting Started

Before contributing:

1. Fork the repository.
2. Clone your fork locally.
3. Create a new branch for your changes.
4. Make and test your changes.
5. Commit your changes with a clear commit message following our conventions.
6. Push the branch to your fork.
7. Open a Pull Request.

Please avoid making unrelated changes in the same Pull Request.

---

## Development Setup

### Prerequisites

Make sure you have the following installed:

- **Java 25** (or higher)
- **Maven** (or use the provided `./mvnw` wrapper)
- **Git**
- **PostgreSQL** (local instance, Docker container, or a hosted Supabase instance)
- An IDE such as IntelliJ IDEA, VS Code, or Eclipse

### Clone the Repository

```bash
git clone https://github.com/Vedant2s/spendwise.git
cd spendwise
```

### Environment Variables & Database Setup

SpendWise reads its database and security configuration directly from environment variables.

Before launching the app, configure the following environment variables in your local environment, IDE Run Configuration, or `.env` file:

| Variable | Description | Example (Local Dev) |
| :--- | :--- | :--- |
| `SUPABASE_DB_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://localhost:5432/spendwise` |
| `SUPABASE_DB_USERNAME` | Database username | `postgres` |
| `SUPABASE_DB_PASSWORD` | Database password | `your_postgres_password` |
| `JWT_SECRET` | 256-bit secret key for signing JWTs | *32+ character random string* |
| `PORT` *(Optional)* | Application server port (defaults to `8081`) | `8081` |

> ⚠️ **Important:** Never commit database passwords, real JWT secrets, or production credentials to the repository.

### Run the Application

Using the Maven wrapper:

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

**Windows (CMD / PowerShell):**
```cmd
mvnw.cmd spring-boot:run
```

By default, the server will start on:
```text
http://localhost:8081
```

*(You can override the port by exporting `PORT=your_port` before starting).*

---

## Creating a Branch

Always branch off from the latest version of the `main` branch:

```bash
git checkout main
git pull origin main
git checkout -b feature/your-feature-name
```

Use descriptive branch prefixes:

- `feature/monthly-budget-alerts`
- `fix/expense-validation`
- `test/account-controller`
- `docs/update-api-documentation`
- `refactor/dashboard-service`

---

## Making Changes

When writing code for SpendWise:

- **Layered Architecture:**
  - Keep **Controllers** focused only on HTTP routing, parameter binding, and response status codes.
  - Keep business logic in the **Service** layer.
  - Keep database operations inside Spring Data JPA **Repositories**.
- **DTOs:** Always use DTOs for request payloads and response bodies. **Never expose JPA persistence entities directly** via API endpoints.
- **Validation:** Use `jakarta.validation` annotations (`@NotNull`, `@Positive`, etc.) on DTOs and validate requests in controllers with `@Valid`.
- **Resource Ownership (Strict):** Ensure users can **only** read, update, or delete financial records (accounts, budgets, transactions) that belong to them. Never rely solely on an entity's primary key ID without verifying ownership against the authenticated user.
- **Dependencies:** Avoid introducing unnecessary third-party libraries.

For large structural or architectural changes, please open an issue first to discuss your approach.

---

## Testing

SpendWise relies on automated tests to maintain code quality. Before submitting a PR, make sure the test suite passes:

**Linux / macOS:**
```bash
./mvnw test
```

**Windows:**
```cmd
mvnw.cmd test
```

When contributing:
- Add unit tests (JUnit 5, Mockito) for new or modified service logic.
- Add controller tests (MockMvc) for new or modified REST endpoints.
- Include test cases for edge cases, validation errors, and unauthorized access attempts.
- If fixing a bug, add a regression test that fails without your fix.

---

## Commit Guidelines

Write clear, structured commit messages following the Conventional Commits style:

```text
type: short description
```

Common types:
- `feat:` A new feature or endpoint
- `fix:` A bug fix
- `test:` Adding or updating tests
- `refactor:` Code changes that neither fix a bug nor add a feature
- `docs:` Documentation-only changes
- `chore:` Build scripts, dependency bumps, or tool configuration

**Examples:**
```text
feat: add monthly spending breakdown endpoint
fix: prevent users from accessing transactions from other accounts
test: add controller tests for budget creation validation
docs: document environment variables in README
```

---

## Pull Requests

When you are ready to submit a PR:

1. Push your branch to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
2. Open a Pull Request against `main` on the upstream repository.
3. Complete the PR description:
   - **What changed?** Summary of code changes.
   - **Why?** Context or motivation behind the change.
   - **Testing:** How you tested the change (and confirmation that `./mvnw test` passes).
   - **Related Issue:** Link any relevant issue (e.g., `Closes #12`).
   - Include sample request/response JSON payloads if you changed an API contract.
4. Ensure no temporary files, `.env` files, or secrets are included in your commit history.

---

## Reporting Bugs

Before reporting a bug, search existing issues to see if it has already been reported.

When filing an issue, please include:
- A clear, concise title.
- Step-by-step instructions to reproduce the issue.
- Expected behavior vs. actual behavior.
- Relevant log output or stack traces (ensure passwords, database strings, and JWTs are redacted).
- Environment details (Java version, OS, local DB vs. Supabase).

---

## Suggesting Features

We welcome ideas that make SpendWise a better financial tool. When suggesting a feature:

- Describe the specific problem or use case the feature solves.
- Outline the proposed behavior and API design (if applicable).
- Keep suggestions aligned with SpendWise's core mission: accessible, reliable personal finance management.

---

## Security

Please **do not** report sensitive security vulnerabilities through public GitHub issues.

Examples of security concerns:
- Authentication or JWT verification bypasses
- Broken Object Level Authorization (BOLA/IDOR) allowing cross-user data access
- SQL injection or remote code execution risks
- Accidental exposure of secrets or credentials

If you discover a vulnerability, please reach out to the project maintainer privately.

---

## Code of Conduct

All contributors and participants are expected to keep discussions constructive, respectful, and professional at all times.

---

## Questions?

Feel free to open an issue or reach out via discussions if you need clarification on an issue or guidance on setting up the project.

Thank you for helping build **SpendWise**! 🚀
