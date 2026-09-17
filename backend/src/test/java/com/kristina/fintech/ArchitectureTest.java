package com.kristina.fintech;

import com.kristina.fintech.model.MonetaryAmount;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ArchitectureTest {
  @Test void monetaryAmountUsesBigDecimalAndImmutableState(){
    assertEquals(BigDecimal.class, field("value").getType());
    assertTrue(java.lang.reflect.Modifier.isFinal(field("value").getModifiers()));
    assertTrue(java.lang.reflect.Modifier.isFinal(field("currencyCode").getModifiers()));
  }
  private Field field(String n){try{return MonetaryAmount.class.getDeclaredField(n);}catch(Exception e){throw new AssertionError(e);}}
}
