package com.kristina.fintech.model;

import java.util.List;
public record ComplianceResult(ComplianceDecision decision, RiskLevel riskLevel, int riskScore, String reason, String ruleVersion, List<TextChunk> evidence, String invoiceHash, String transactionId, String paymentHash) {}
