# Architecture Decision Record

## 1. Trust boundaries

1. React is an untrusted client.
2. Spring Security authenticates the caller.
3. Invoice XML is parsed with XXE protections before AI.
4. Payment PAN is tokenized at the edge and is never passed into the financial domain.
5. Local AI is advisory only.
6. Deterministic validation/rules decide the compliance outcome.
7. Risk policy can downgrade an approval to human review.
8. Only an authorized decision can reach a payment rail.
9. SHA-256 proves the exact invoice payload represented by the audit event.
10. The demo ledger stores proof metadata, not the invoice or PAN.

## 2. AI boundary

The local Ollama service receives structured invoice data and retrieved evidence. AI output is treated as an advisory note. A timeout, hallucination or unavailable model cannot produce an APPROVE decision.

## 3. Tokenization boundary

`TokenizationService` performs HMAC-based demo tokenization. This is intentionally not described as an HSM-backed PCI vault. Production should replace it with a certified tokenization/vault service and keep PAN outside logs, persistence and ordinary application objects.

## 4. Money boundary

`MonetaryAmount` owns currency and scale. All money enters the domain as `BigDecimal`; no `double`/`float` is used for financial amounts. `HALF_EVEN` is a deterministic default, not a universal legal requirement; business rules may select another rounding policy.

## 5. Blockchain boundary

The project contains a local append-only ledger and a Web3j-ready dependency for future external anchoring. It deliberately stores hashes and metadata rather than invoice contents or personal/payment data.

## 6. Payment boundary

`PaymentRail` separates the compliance domain from settlement. `StellarPaymentAdapter` is simulation-first. Production signing must be isolated behind an HSM/secret manager, with idempotency, limits, reconciliation and independent authorization.
