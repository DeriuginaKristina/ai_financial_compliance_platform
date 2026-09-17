package com.kristina.fintech.model;

public record ComplianceAiAssessment(String riskNote, String confidence, String modelVersion, boolean suspiciousInput) {}
