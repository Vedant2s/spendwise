# Security Policy

## Supported Versions

SpendWise is currently under active development.

Security updates are provided for the latest version of the application. Older commits, development branches, forks, and unofficial deployments are not actively maintained.

| Version | Supported |
| --- | --- |
| Latest release / main | ✅ |
| Older versions | ❌ |

As the project matures and introduces versioned releases, this section may be updated to describe support for individual release versions.

## Reporting a Vulnerability

The security of SpendWise is taken seriously.

If you discover a security vulnerability, **please do not report it through a public GitHub issue, discussion, or pull request**, especially if publicly disclosing the issue could put users, authentication mechanisms, or sensitive data at risk.

Instead, please report the vulnerability privately by contacting:

**Email:** vedantsv1@gmail.com

When reporting a vulnerability, please include as much of the following information as possible:

- A description of the vulnerability
- The affected endpoint, component, or functionality
- Steps required to reproduce the issue
- The potential security impact
- Relevant logs, screenshots, or proof\-of\-concept details
- Suggested remediation, if known

Please **do not include real passwords, JWTs, API keys, database credentials, or personal financial information** in your report.

## Security Issues of Particular Interest

Since SpendWise handles authentication and personal financial data, reports involving the following areas are particularly important:

- Authentication or authorization bypasses
- Broken user\-level data isolation
- Access to another user's accounts, expenses, budgets, goals, or other financial data
- JWT validation or authentication vulnerabilities
- Exposure of passwords, secrets, tokens, or database credentials
- SQL injection or other injection vulnerabilities
- Improper input validation with security implications
- Sensitive financial information appearing in logs or API responses
- Unauthorized modification or deletion of another user's data
- Security misconfigurations that expose protected API endpoints

## What to Expect After Reporting

After receiving a vulnerability report:

1. The report will be reviewed to determine whether the issue can be reproduced.
2. The severity and potential impact of the vulnerability will be assessed.
3. If confirmed, a fix will be developed and tested.
4. The reporter may be contacted for additional information if required.
5. Once the issue has been resolved, information about the vulnerability may be disclosed publicly when it is safe and appropriate to do so.

As SpendWise is currently maintained as an independent project, response times may vary. Reasonable efforts will be made to acknowledge legitimate security reports within **7 days**.

## Responsible Disclosure

Please allow reasonable time for a reported vulnerability to be investigated and fixed before publicly disclosing it.

Researchers acting in good faith are encouraged to:

- Avoid accessing or modifying data that does not belong to them
- Avoid intentionally degrading or disrupting the service
- Use test accounts and test data whenever possible
- Collect only the information necessary to demonstrate the vulnerability
- Stop testing if sensitive information belonging to another user is encountered
- Report discovered vulnerabilities privately

## Security Best Practices for Contributors

Contributors should never commit sensitive information to the repository.

This includes:

- Database passwords
- JWT signing secrets
- API keys
- Access or refresh tokens
- Cloud credentials
- Production connection strings
- .env files containing secrets
- Private user or financial data

Secrets required by the application should be supplied through environment variables or the appropriate deployment secret\-management mechanism.

Security\-related functionality involving authentication, authorization, user ownership, or access to financial data should include appropriate tests when possible.

## Scope

This security policy applies to the official SpendWise backend repository and its latest supported version.

Security vulnerabilities in third\-party dependencies may also be reported when they directly affect SpendWise.

Issues affecting modified forks, unofficial deployments, unsupported versions, or unrelated third\-party infrastructure may fall outside the scope of this policy.

## Thank You

Responsible vulnerability reports help make SpendWise safer for everyone.

Thank you for taking the time to report security issues responsibly.

