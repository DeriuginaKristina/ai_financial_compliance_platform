package com.kristina.fintech.controller;

import com.kristina.fintech.approval.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/approvals")
public class ApprovalController {
  private final ApprovalService approval;
  public ApprovalController(ApprovalService approval){this.approval=approval;}
  @PostMapping("/{transactionId}") public ResponseEntity<String> approve(@PathVariable String transactionId){approval.humanApprove(transactionId);return ResponseEntity.ok("HUMAN_APPROVED");}
}
