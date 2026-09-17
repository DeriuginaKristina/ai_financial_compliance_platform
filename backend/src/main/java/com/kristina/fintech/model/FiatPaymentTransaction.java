package com.kristina.fintech.model;

public final class FiatPaymentTransaction extends FinancialTransaction {
  private final String cardHolderName;
  private final String token;
  public FiatPaymentTransaction(MonetaryAmount amount, String cardHolderName, String token) {
    super(amount); this.cardHolderName = cardHolderName; this.token = token;
  }
  public String getCardHolderName() { return cardHolderName; }
  public String getToken() { return token; }
  @Override public boolean validateIngestionPerimeter() { return getAmount().isPositive() && getAmount().getValue().scale() == 2 && token != null && token.startsWith("tok_"); }
}
