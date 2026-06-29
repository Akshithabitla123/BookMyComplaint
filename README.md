# BookMyComplaint

A web-based complaint management system that lets citizens register complaints and admins triage, assign, and resolve them — with automatic priority-based escalation for complaints left unresolved too long.

## Tech Stack

- **Backend:** Spring Boot, Spring MVC, Spring Data JPA, Hibernate
- **Database:** MySQL
- **Frontend:** HTML, CSS, JavaScript
- **Validation:** Jakarta Bean Validation (`@NotBlank`)
- **Scheduling:** Spring `@Scheduled` for the auto-escalation job

## Features

- Citizen registration, login, and complaint submission
- Admin dashboard with filtering (by title, area, status) and pagination
- Staff assignment workflow
- **Priority-aware auto-escalation** — complaints left unresolved past a priority-based threshold are automatically flagged for admin attention
- Centralized exception handling with structured error responses (no generic 500s for expected failures)
- Role-based UI routing (separate citizen/admin dashboards based on logged-in user's role)

## Complaint Status Flow

A complaint moves through the following states:

- A complaint starts as `OPEN`.
- If an admin assigns staff before it ages out, it moves directly to `IN_PROGRESS`.
- If it sits `OPEN` past its priority's threshold, the scheduled escalation job flags it `ESCALATED`.
- An `ESCALATED` complaint **cannot** have staff assigned directly — an admin must call the review endpoint first, which moves it to `IN_PROGRESS`.It forces a visible admin decision on overdue complaints rather than letting assignment silently clear the escalation flag.
- From `IN_PROGRESS`, a complaint can be marked `RESOLVED` or `REJECTED`.
- `RESOLVED` and `REJECTED` are terminal — no further status changes are allowed.

## Auto-Escalation Logic

A scheduled job (`@Scheduled`, runs hourly) sweeps all `OPEN` complaints and escalates any that have aged past their priority's threshold:

| Priority | Escalation threshold |
|----------|----------------------|
| HIGH     | 1 day                |
| MEDIUM   | 2 days               |
| LOW      | 3 days               |

A manual trigger endpoint (`POST /complaints/admin/escalate/run`) is also available, so the sweep can be tested on demand instead of waiting for the hourly schedule — useful for demos and verification.

## API Overview

13 REST endpoints across two resources:

**Complaints** (`/complaints`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/user/{id}` | Citizen creates a complaint |
| GET | `/user/{id}` | Get a citizen's complaints (paginated) |
| GET | `/admin/complaints` | Get all complaints (admin) |
| PUT | `/admin/{id}/status` | Update complaint status |
| PUT | `/admin/{id}/assign` | Assign staff to a complaint |
| PUT | `/admin/{id}/review` | Clear an escalation, move to `IN_PROGRESS` |
| GET | `/admin/filter/area` | Filter by area |
| GET | `/admin/filter/status` | Filter by status |
| GET | `/admin/filter/title` | Filter by title |
| GET | `/admin/details/{id}` | Get full complaint + user details |
| POST | `/admin/escalate/run` | Manually trigger the escalation sweep |

**Users** (`/users`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Register a new user |
| GET | `/` | Get all users |
| GET | `/user/{id}` | Get user by ID |
| POST | `/user/login` | Login with email + password |