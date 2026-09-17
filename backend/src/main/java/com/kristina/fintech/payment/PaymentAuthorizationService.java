package com.kristina.fintech.payment;

import com.kristina.fintech.model.FiatPaymentTransaction;
import org.springframework.stereotype.Service;

/** Central policy boundary: a denied transaction can never reach a payment rail. */
@Service
public class PaymentAuthorizationService {
  private final PaymentRail rail;
  public PaymentAuthorizationService(PaymentRail rail){this.rail=rail;}

  public String authorize(FiatPaymentTransaction tx, String decision, boolean humanApproved) {
    if ("DENY".equalsIgnoreCase(decision)) throw new PaymentPolicyException("BLOCKED_BY_POLICY");
    if ("REVIEW".equalsIgnoreCase(decision) && !humanApproved) throw new PaymentPolicyException("HUMAN_APPROVAL_REQUIRED");
    return rail.execute(tx);
  }
  public static class PaymentPolicyException extends RuntimeException { public PaymentPolicyException(String message){super(message);} }
}
