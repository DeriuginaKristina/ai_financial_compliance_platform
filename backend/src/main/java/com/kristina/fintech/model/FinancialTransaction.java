package com.kristina.fintech.model;

import java.time.Instant;
import java.util.UUID;

public abstract class FinancialTransaction {
  private final String transactionId = UUID.randomUUID().toString();
  private final MonetaryAmount amount;
  private final Instant timestamp = Instant.now();
  protected FinancialTransaction(MonetaryAmount amount) { this.amount = amount; }
  public String getTransactionId() { return transactionId; }
  public MonetaryAmount getAmount() { return amount; }
  public Instant getTimestamp() { return timestamp; }
  public abstract boolean validateIngestionPerimeter();
}
