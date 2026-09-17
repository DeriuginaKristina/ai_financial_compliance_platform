# Test Strategy

## Security invariants

1. Raw PAN must never appear in application logs.
2. Raw PAN must not cross the tokenization boundary into the financial core.
3. `DENY` must never invoke `PaymentRail`.
4. `REVIEW` without human approval must never invoke `PaymentRail`.
5. Suspicious AI instruction patterns must not become payment authorization.
6. Audit-chain verification must fail after tampering.
7. XML parser must reject XXE/unsafe external entities.

## Test layers

- Unit: monetary precision, risk scoring, prompt injection, policy decisions.
- Architecture: dependency rules and forbidden floating-point financial arithmetic.
- Integration: Spring Security, invoice ingestion, compliance workflow.
- Contract: typed AI response schema.
- Security regression: PAN/logging, XML parser, authorization boundaries.
- Failure tests: Ollama unavailable, retrieval unavailable, payment rail unavailable, blockchain unavailable.
- End-to-end: invoice upload -> validation -> AI -> compliance -> risk -> approval -> audit -> payment.

## CI/CD quality gates

- Tests must pass.
- SAST and dependency vulnerability scan.
- Secret scanning.
- Container image scan.
- No raw PAN in test logs/artifacts.
- Architecture tests pass.
- Audit integrity tests pass.
