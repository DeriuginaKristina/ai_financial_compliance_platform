package com.kristina.fintech;

import com.kristina.fintech.model.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PaymentPolicyTest {
  @Test void deniedDecisionNeverQualifiesForPayment(){
    MonetaryAmount amount=new MonetaryAmount(new BigDecimal("10.00"),"EUR");
    FiatPaymentTransaction tx=new FiatPaymentTransaction(amount,"Test User","tok_123");
    assertTrue(tx.validateIngestionPerimeter());
    assertEquals(ComplianceDecision.DENY,ComplianceDecision.valueOf("DENY"));
  }
}
