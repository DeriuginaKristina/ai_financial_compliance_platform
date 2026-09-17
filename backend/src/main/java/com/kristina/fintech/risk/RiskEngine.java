package com.kristina.fintech.risk;

import com.kristina.fintech.compliance.ComplianceEngine.Draft;
import com.kristina.fintech.model.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class RiskEngine {
  public Draft apply(Draft d, BigDecimal amount) {
    if (amount != null && amount.compareTo(new BigDecimal("10000")) >= 0 && d.decision()==ComplianceDecision.APPROVE)
      return new Draft(ComplianceDecision.REVIEW,RiskLevel.MEDIUM,35,"Amount threshold requires human review",d.ruleVersion(),d.evidence(),d.invoiceHash(),d.transactionId(),d.aiNote());
    return d;
  }
}
