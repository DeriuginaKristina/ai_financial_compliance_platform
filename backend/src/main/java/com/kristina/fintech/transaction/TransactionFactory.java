package com.kristina.fintech.transaction;

import com.kristina.fintech.model.*;
import org.springframework.stereotype.Component;

@Component
public class TransactionFactory {
  public FiatPaymentTransaction create(MonetaryAmount amount,String holder,String token){return new FiatPaymentTransaction(amount,holder,token);}
}
