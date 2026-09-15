# Solution design pack

## System boundary

```text
Student / Trainer / Placement Officer
             │ HTTPS + JWT
Static web client ──► Spring Boot API ──► PostgreSQL
                         │       │
                         │       └── audit events
                         └── Python recommendation service
                                   │
                              synthetic model/data
```

The browser only receives data belonging to its authenticated user. The placement-officer aggregate endpoint deliberately returns role-level counts and averages, not individual student records.

## Key data contracts

| Endpoint | Authorization | Purpose |
| --- | --- | --- |
| `POST /api/auth/register`, `/login` | public | Creates a student or issues a JWT. Self-registration cannot choose a privileged role. |
| `GET /api/roles` | public | Role competencies and readiness targets. |
| `GET /api/me/dashboard` | student | Current diagnostic, learning-plan and interview state. |
| `POST /api/diagnostics` | student | Immutable diagnostic completion event and topic scores. |
| `POST/PATCH /api/learning-plan` | student | Adds or marks an owned learning item complete. |
| `POST /api/interviews` | student | Adds a mock-interview reflection. |
| `GET /api/officer/readiness` | placement officer | De-identified aggregate readiness by role. |

Swagger documentation is available at `/swagger-ui.html`; the OpenAPI document is `/v3/api-docs`.

## State flow

```text
Diagnostic submitted → topic scores persisted → gap calculation / AI recommendation
     → learner accepts and completes plan tasks → mock interview log
     → new diagnostic → readiness trend and aggregate officer metric
```

Each student mutation produces an `AuditEvent` containing actor, action, resource and timestamp. Score history is append-only rather than overwriting a prior assessment.

## Threat model and controls

| Threat | Control |
| --- | --- |
| Password theft | BCrypt password hashes; passwords are never returned. |
| Privilege escalation | JWT role checks; public registration always creates `STUDENT`. |
| Accessing another learner's task | Repository queries bind update lookups to the authenticated student. |
| Invalid/malicious input | Bean validation, size limits and generic error responses. |
| Token replay | Short JWT expiry; production deployment must use TLS and a rotated `APP_JWT_SECRET`. |
| Data overexposure | Officer view is aggregated; no raw student data is sent there. |
| AI misuse | Recommendation only, synthetic data, transparent baseline and explicit no-hiring-decision policy. |

Before assessment use, add consent text, retention/deletion workflows, organisation-specific CORS origins and an API gateway rate limit. Do not commit `.env` files or real learner data.

## Evaluation plan and acceptance gates

- Assessment reliability: same fixed response set produces the same score; target ≥99% deterministic scoring checks.
- Learning-plan completion: completed tasks / assigned tasks, measured per cohort and role.
- Improvement: median latest diagnostic minus first diagnostic; target ≥10 points after four weeks (reported with sample size).
- Security: unauthenticated protected requests return 401; cross-student plan update returns 404; zero secrets in repository scan.
- Scalability: use k6 to exercise dashboard and role aggregate endpoints; go/no-go target p95 <500 ms at an agreed class-demo load.
- Robustness: test invalid 0–100 scores, duplicate email, expired token, unavailable AI service and empty learner history.

## AI comparison study

The baseline ranks `target - diagnostic score`. The innovation service uses a deterministic Random Forest fitted only on explicitly labelled synthetic data to produce a readiness signal while retaining baseline gap explanations. Compare the two ranking outputs on a held-out synthetic attempt set using top-3 weak-topic agreement, latency and failure behaviour. The model is not a hiring model and its result is advisory.
