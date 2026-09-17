package com.kristina.fintech;

import com.kristina.fintech.model.MonetaryAmount;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class FinancialPrecisionTest {
  @Test void fiatUsesCurrencyScaleAndDeterministicRounding(){
    MonetaryAmount amount=new MonetaryAmount(new BigDecimal("100.555"),"EUR");
    assertEquals("100.56",amount.getValue().toPlainString()); assertEquals(2,amount.getValue().scale());
  }
  @Test void currencyMismatchIsRejected(){
    assertThrows(IllegalArgumentException.class,()->new MonetaryAmount(new BigDecimal("1"),"EUR").add(new MonetaryAmount(new BigDecimal("1"),"USD")));
  }
}
