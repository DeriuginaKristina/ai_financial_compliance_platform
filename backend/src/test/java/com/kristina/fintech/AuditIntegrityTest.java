package com.kristina.fintech;

import com.kristina.fintech.ledger.BlockchainLedger;
import com.kristina.fintech.model.AuditRecord;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class AuditIntegrityTest {
  @Test void auditChainIsTamperEvident() {
    BlockchainLedger ledger=new BlockchainLedger();
    AuditRecord a=new AuditRecord("e1","tx1","system","COMPLIANCE","APPROVE","LOW","2026.09","test-model","abc",Instant.parse("2026-09-06T10:00:00Z"),null,null,null);
    AuditRecord sealed=ledger.anchor(a);
    assertTrue(ledger.verifyChain());
    assertEquals("GENESIS", sealed.previousHash());
    assertNotNull(sealed.currentHash());
  }
}
