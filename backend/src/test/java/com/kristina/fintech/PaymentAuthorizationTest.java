package com.kristina.fintech;

import com.kristina.fintech.model.*;
import com.kristina.fintech.payment.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentAuthorizationTest {
  @Test void denyNeverReachesPaymentRail() {
    PaymentRail rail=Mockito.mock(PaymentRail.class);
    PaymentAuthorizationService service=new PaymentAuthorizationService(rail);
    FiatPaymentTransaction tx=new FiatPaymentTransaction(new MonetaryAmount(new BigDecimal("100.00"),"EUR"),"Alice","tok_test");
    assertThrows(PaymentAuthorizationService.PaymentPolicyException.class, () -> service.authorize(tx,"DENY",false));
    Mockito.verifyNoInteractions(rail);
  }

  @Test void reviewRequiresHumanApproval() {
    PaymentRail rail=Mockito.mock(PaymentRail.class);
    PaymentAuthorizationService service=new PaymentAuthorizationService(rail);
    FiatPaymentTransaction tx=new FiatPaymentTransaction(new MonetaryAmount(new BigDecimal("100.00"),"EUR"),"Alice","tok_test");
    assertThrows(PaymentAuthorizationService.PaymentPolicyException.class, () -> service.authorize(tx,"REVIEW",false));
    Mockito.verifyNoInteractions(rail);
  }
}
