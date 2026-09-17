package com.kristina.fintech.payment;

import com.kristina.fintech.model.FinancialTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class StellarPaymentAdapter implements PaymentRail {
  private final boolean enabled;
  public StellarPaymentAdapter(@Value("${app.payment.stellar.enabled}") boolean enabled){this.enabled=enabled;}
  @Override public String execute(FinancialTransaction tx){
    if(!enabled) return "SIMULATED_STELLAR_TESTNET_"+UUID.randomUUID();
    // Deliberately no private key is accepted through the API. Production signing belongs behind an HSM/secret manager.
    return "STELLAR_ADAPTER_ENABLED_BUT_SIGNING_NOT_CONFIGURED";
  }
}
