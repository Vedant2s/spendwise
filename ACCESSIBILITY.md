# Accessibility

SpendWise aims to be usable and understandable by as many people as reasonably possible, regardless of ability, technology, or experience level.

SpendWise is currently a **backend\-first personal finance application**, with a Spring Boot REST API forming the primary supported component. As user\-facing interfaces are developed, accessibility will be considered as part of their design and implementation.

This document describes the project's current accessibility priorities, expectations for contributors, and the process for reporting accessibility barriers.

## Priorities

SpendWise prioritizes:

- Clear and understandable API documentation
- Consistent API responses and error messages
- Human\-readable validation messages
- Documentation written in clear and straightforward language
- Avoiding unnecessary reliance on visual information alone
- Accessible user\-facing interfaces as frontend functionality develops
- Keyboard accessibility for interactive frontend functionality
- Appropriate labels and semantic structure for forms and controls
- Sufficient color contrast and alternatives to color\-only indicators

For future user\-facing interfaces, SpendWise aims to follow the principles of **WCAG 2.2 Level AA** where practical.

This is an **aspirational target and not a claim of WCAG conformance**. Formal accessibility testing or certification has not currently been performed.

## Contributor Expectations

Contributors should consider accessibility whenever a change affects how users interact with or understand SpendWise.

### Backend and API changes

Contributors should:

- Keep validation and error messages clear and understandable.
- Avoid exposing unnecessarily technical implementation details to API consumers.
- Maintain consistent API response structures where practical.
- Document new or changed endpoints.
- Clearly describe required fields, validation requirements, and expected errors.

### Frontend and user\-facing changes

When contributing user\-facing functionality, contributors should:

- Use semantic HTML elements where appropriate.
- Associate form inputs with meaningful labels.
- Ensure interactive functionality can be operated using a keyboard where reasonably possible.
- Avoid using color as the only way to communicate information.
- Provide meaningful alternative text for informative images.
- Avoid unnecessary animations or interactions that may make content difficult to use.
- Maintain sufficient contrast between important text, controls, and their backgrounds.
- Preserve visible keyboard focus indicators.
- Use descriptive button, link, and heading text.

When submitting a Pull Request containing significant user\-facing changes, contributors are encouraged to describe any accessibility considerations or testing performed.

## Reporting Accessibility Issues

Accessibility barriers can be reported through a **GitHub issue**.

When reporting an accessibility problem, please provide whatever information is available, such as:

- The page, feature, endpoint, or task affected
- What you were attempting to accomplish
- What happened instead
- Steps that reproduce the problem
- Browser and operating system, when relevant
- Keyboard\-only behavior, when relevant
- Assistive technology and version, if relevant

Screenshots or recordings may be helpful but are **not required**.

You do **not** need to disclose a disability or medical condition when reporting an accessibility issue.

If an accessibility report contains sensitive security information, credentials, private financial data, or reveals a security vulnerability, please follow the process described in SECURITY.md instead of creating a public issue.

### Severity

Accessibility issues may be categorized based on how significantly they prevent someone from using SpendWise.

| Severity | Description |
| --- | --- |
| **Critical** | A barrier prevents access to essential functionality with no reasonable workaround. |
| **High** | An important task is inaccessible or extremely difficult to complete. |
| **Medium** | Functionality remains available, but an accessibility barrier makes the experience significantly harder to use. |
| **Low** | A minor accessibility problem that does not prevent completion of the affected task. |

Reporters do not need to determine severity before submitting an issue. Severity can be assigned during issue triage.

## How We Respond

When an accessibility issue is reported:

1. The issue will be reviewed and reproduced when possible.
2. The impact of the barrier will be assessed.
3. Available workarounds may be documented while a permanent solution is developed.
4. Accessibility problems affecting essential functionality will be prioritized appropriately.
5. The fix may be verified with the reporter when practical.

SpendWise is currently maintained as an independent project, so resolution times may vary depending on the complexity of the issue.

Accessibility reports are nevertheless treated as legitimate functional issues rather than cosmetic requests.

## Ownership and Maintenance

Accessibility is currently maintained as part of the overall SpendWise project rather than by a dedicated accessibility team.

The project maintainer is responsible for:

- Reviewing reported accessibility barriers
- Considering accessibility when reviewing user\-facing contributions
- Keeping this accessibility statement reasonably aligned with the current state of the project
- Updating accessibility requirements as the SpendWise frontend and user experience mature

This document should be reviewed when significant user\-facing functionality or platform support changes.

## Supported Environments

SpendWise is currently primarily a **REST API**, so traditional browser and assistive\-technology compatibility does not apply to much of the backend functionality.

API functionality is intended to be consumable through standard HTTP clients and compatible frontend applications.

As the SpendWise frontend develops, tested browsers, devices, input methods, and assistive technologies can be documented here.

Until such testing has been performed, this project does not claim support or accessibility conformance for specific combinations of:

- Browsers
- Operating systems
- Screen readers
- Mobile devices
- Voice\-control software
- Other assistive technologies

## Known Limitations

SpendWise has not undergone a formal accessibility audit.

Because the project is currently backend\-first, accessibility testing of a complete end\-user interface is also limited.

As the frontend develops, areas requiring particular attention include:

- Keyboard navigation
- Screen\-reader behavior
- Form labels and validation feedback
- Focus management
- Color contrast
- Responsive layouts and zoom
- Accessible charts and financial visualizations

These areas should be tested as the associated user\-facing functionality is implemented rather than assumed to be accessible.

Known accessibility barriers should be tracked through GitHub issues whenever possible.

## Feedback and Improvements

Feedback about the accessibility of SpendWise is welcome.

Suggestions for improving this accessibility statement, documentation, development practices, or user experience can be submitted through GitHub issues or Pull Requests.

If you encounter an active accessibility barrier, please report it using the accessibility issue\-reporting process described above.

Accessibility is an ongoing part of improving SpendWise, and this document will evolve alongside the project.

