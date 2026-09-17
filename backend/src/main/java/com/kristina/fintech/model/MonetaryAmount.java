package com.kristina.fintech.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class MonetaryAmount {
  private final BigDecimal value;
  private final String currencyCode;
  public MonetaryAmount(BigDecimal value, String currencyCode) {
    if (value == null || currencyCode == null || currencyCode.isBlank()) throw new IllegalArgumentException("Amount and currency are required");
    String code = currencyCode.toUpperCase();
    try { Currency.getInstance(code); } catch (IllegalArgumentException e) { throw new IllegalArgumentException("Unsupported currency: " + code); }
    this.currencyCode = code;
    this.value = value.setScale(Currency.getInstance(code).getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
  }
  public BigDecimal getValue() { return value; }
  public String getCurrencyCode() { return currencyCode; }
  public MonetaryAmount add(MonetaryAmount other) { requireSameCurrency(other); return new MonetaryAmount(value.add(other.value), currencyCode); }
  public boolean isPositive() { return value.signum() > 0; }
  private void requireSameCurrency(MonetaryAmount other) { if (!currencyCode.equals(other.currencyCode)) throw new IllegalArgumentException("Currency mismatch"); }
  @Override public String toString() { return value.toPlainString() + " " + currencyCode; }
  @Override public boolean equals(Object o) { if (!(o instanceof MonetaryAmount m)) return false; return value.equals(m.value) && currencyCode.equals(m.currencyCode); }
  @Override public int hashCode() { return Objects.hash(value, currencyCode); }
}
