package com.kristina.fintech.controller;

import com.kristina.fintech.audit.AuditService;
import com.kristina.fintech.compliance.ComplianceEngine;
import com.kristina.fintech.model.*;
import com.kristina.fintech.invoice.InvoiceParser;
import com.kristina.fintech.risk.RiskEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.UUID;

@RestController @RequestMapping("/api/invoices")
public class InvoiceController {
  private final InvoiceParser parser; private final ComplianceEngine compliance; private final RiskEngine risk; private final AuditService audit;
  public InvoiceController(InvoiceParser parser,ComplianceEngine compliance,RiskEngine risk,AuditService audit){this.parser=parser;this.compliance=compliance;this.risk=risk;this.audit=audit;}
  @PostMapping(value="/verify", consumes={"application/xml","text/xml"})
  public ResponseEntity<ComplianceResult> verify(@RequestBody String xml){
    String txId="tx_"+UUID.randomUUID(); String hash=audit.hash(xml); InvoiceData invoice=parser.parse(xml);
    ComplianceEngine.Draft d=risk.apply(compliance.evaluate(invoice,hash,txId),invoice.payableAmount());
    ComplianceResult result=new ComplianceResult(d.decision(),d.riskLevel(),d.riskScore(),d.reason()+"; AI="+d.aiNote(),d.ruleVersion(),d.evidence(),hash,txId,null);
    audit.record(result); return ResponseEntity.ok(result);
  }
}
