# AI Financial Compliance & Transaction Security Platform

Enterprise-style portfolio MVP combining deterministic financial controls with local AI, secure invoice ingestion, tokenization, audit hashing, a blockchain-ready ledger and payment-rail abstraction.

> **Principle:** Let AI reason. Let deterministic software enforce. Let security layers refuse. Let humans authorize risk. Let cryptography prove what happened.

## Architecture

React + Redux → Spring Security → Invoice/Payment Edge → deterministic XML validation → PAN tokenization → local AI → hybrid retrieval → compliance engine → risk engine → human review/auto-approval → SHA-256 audit proof → ledger anchor → payment rail.

### What is implemented
- Spring Boot 3.3 / Java 17 backend.
- Spring Security RBAC with configurable credentials.
- ZUGFeRD/XRechnung-oriented XML ingestion and secure XML parsing (MVP supports the XML payload directly; full PDF/A-3 embedded XML extraction is a future adapter).
- Deterministic invoice validation before AI.
- Local Ollama integration; AI failure never becomes an approval.
- Hybrid retrieval: SQLite FTS5 plus an in-memory vector index.
- Compliance rules with version/effective date/source metadata.
- Risk scoring: LOW → auto approval, MEDIUM → human review, HIGH → block.
- `MonetaryAmount` value object using `BigDecimal` and explicit scale/rounding.
- `FinancialTransaction` / `FiatPaymentTransaction` domain abstraction.
- PAN tokenization at the payment edge; PAN is not stored in the transaction domain.
- Regression test proving PAN is absent from captured application logs.
- Payment rail abstraction with a safe simulated Stellar testnet adapter.
- SHA-256 audit hash and local blockchain-style append-only ledger.
- No invoice/PII is written to the ledger; only cryptographic proof and metadata.
- React dashboard with Redux Toolkit.
- Docker Compose including optional local Ollama.

## Security notes
This is a portfolio/demo architecture, not a PCI DSS-certified token vault, HSM, tax filing system, or legal compliance determination. Never use real PANs, private keys or production credentials in this repository.

The demo payment rail is disabled by default and uses simulation. A production integration should use a proper secret manager/HSM, transaction signing controls, idempotency, reconciliation and independent authorization.

## Run locally

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

### Docker
```bash
docker compose up --build
```

Ollama is optional. If unavailable, the platform falls back to deterministic compliance logic and never upgrades a decision to APPROVE merely because the AI is unavailable.

## Demo credentials
Username: `accountant`  
Password: `change-me`

Change them through environment variables before any real deployment.

## API
- `POST /api/invoices/verify` — verify invoice XML and run compliance pipeline.
- `POST /api/payments/tokenize` — tokenize a PAN at the edge.
- `POST /api/payments/authorize` — evaluate a payment and authorize only if policy permits.
- `GET /api/audit/{transactionId}` — retrieve the audit record.

## Example invoice
Use an XML document containing fields such as invoice number, supplier/buyer identifiers, issue date, currency, tax total and payable amount. The parser accepts common UBL-style `Invoice` documents and rejects unsafe XML features such as DOCTYPE/external entities.

## Tests
```bash
cd backend
mvn test
```
Key regression tests include financial precision, tokenization/log safety, XML XXE rejection, compliance outcomes and the rule that a denied transaction never reaches the payment adapter.


## Security & FinTech assessment

This project is intentionally positioned as a **security-first financial control plane**, not as a generic AI chatbot or blockchain demo. The architecture separates AI reasoning from financial authorization:

`Invoice -> Security -> Deterministic Validation -> Local AI -> Compliance -> Risk -> Human/Auto Approval -> Cryptographic Audit -> PaymentRail`

Key controls now included in the MVP codebase:
- BigDecimal financial value object and transaction domain.
- Prompt-injection perimeter guard.
- Typed local-AI assessment with model version.
- Central payment authorization boundary where DENY cannot call a payment rail.
- Append-only in-memory audit hash chain with verification.
- Actuator health/metrics endpoints.
- Expanded security regression tests.

See `docs/SECURITY_ASSESSMENT.md` for the detailed assessment and remaining enterprise gaps.
