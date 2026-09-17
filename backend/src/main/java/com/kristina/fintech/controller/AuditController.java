package com.kristina.fintech.controller;

import com.kristina.fintech.audit.AuditService;
import com.kristina.fintech.model.AuditRecord;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/audit")
public class AuditController {
  private final AuditService audit; public AuditController(AuditService audit){this.audit=audit;}
  @GetMapping("/{transactionId}") public ResponseEntity<AuditRecord> get(@PathVariable String transactionId){return audit.find(transactionId).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
}
