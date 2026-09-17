package com.kristina.fintech.audit;

import com.kristina.fintech.ledger.BlockchainLedger;
import com.kristina.fintech.model.*;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class AuditService {
  private final BlockchainLedger ledger;
  public AuditService(BlockchainLedger ledger){this.ledger=ledger;}

  public String hash(String xml){
    try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(xml.getBytes(StandardCharsets.UTF_8))); }
    catch(Exception e){ throw new IllegalStateException("SHA-256 unavailable", e); }
  }

  public AuditRecord record(ComplianceResult result) { return record(result, "system", "COMPLIANCE_DECISION"); }

  public AuditRecord record(ComplianceResult result, String actor, String action) {
    AuditRecord event = new AuditRecord(UUID.randomUUID().toString(), result.transactionId(), actor, action, result.decision().name(), result.riskLevel().name(), result.ruleVersion(), "unknown", result.invoiceHash(), Instant.now(), result.paymentHash(), null, null);
    return ledger.anchor(event);
  }

  public java.util.Optional<AuditRecord> find(String id){return ledger.find(id);}
  public boolean verifyChain(){return ledger.verifyChain();}
}
