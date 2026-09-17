package com.kristina.fintech.ledger;

import com.kristina.fintech.model.AuditRecord;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/** Demo append-only audit ledger with a hash chain. A production deployment should persist it. */
@Service
public class BlockchainLedger {
  private final List<AuditRecord> records = new CopyOnWriteArrayList<>();

  public synchronized AuditRecord anchor(AuditRecord unsigned) {
    String previous = records.isEmpty() ? "GENESIS" : records.get(records.size()-1).currentHash();
    String current = sha256(unsigned.eventId()+"|"+unsigned.transactionId()+"|"+unsigned.actor()+"|"+unsigned.action()+"|"+unsigned.decision()+"|"+unsigned.riskLevel()+"|"+unsigned.ruleVersion()+"|"+unsigned.modelVersion()+"|"+unsigned.invoiceHash()+"|"+unsigned.timestamp()+"|"+unsigned.paymentHash()+"|"+previous);
    AuditRecord sealed = new AuditRecord(unsigned.eventId(), unsigned.transactionId(), unsigned.actor(), unsigned.action(), unsigned.decision(), unsigned.riskLevel(), unsigned.ruleVersion(), unsigned.modelVersion(), unsigned.invoiceHash(), unsigned.timestamp(), unsigned.paymentHash(), previous, current);
    records.add(sealed);
    return sealed;
  }

  public Optional<AuditRecord> find(String id) { return records.stream().filter(r -> r.transactionId().equals(id)).findFirst(); }
  public List<AuditRecord> all() { return List.copyOf(records); }

  public boolean verifyChain() {
    String previous = "GENESIS";
    for (AuditRecord r : records) {
      if (!Objects.equals(previous, r.previousHash())) return false;
      String expected = sha256(r.eventId()+"|"+r.transactionId()+"|"+r.actor()+"|"+r.action()+"|"+r.decision()+"|"+r.riskLevel()+"|"+r.ruleVersion()+"|"+r.modelVersion()+"|"+r.invoiceHash()+"|"+r.timestamp()+"|"+r.paymentHash()+"|"+r.previousHash());
      if (!Objects.equals(expected, r.currentHash())) return false;
      previous = r.currentHash();
    }
    return true;
  }

  private String sha256(String value) {
    try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
    catch (Exception e) { throw new IllegalStateException("SHA-256 unavailable", e); }
  }
}
