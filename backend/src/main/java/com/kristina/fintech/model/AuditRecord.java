package com.kristina.fintech.model;

import java.time.Instant;

public record AuditRecord(
    String eventId,
    String transactionId,
    String actor,
    String action,
    String decision,
    String riskLevel,
    String ruleVersion,
    String modelVersion,
    String invoiceHash,
    Instant timestamp,
    String paymentHash,
    String previousHash,
    String currentHash) {}
