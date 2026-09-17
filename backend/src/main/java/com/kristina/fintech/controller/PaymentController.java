package com.kristina.fintech.controller;

import com.kristina.fintech.model.*;
import com.kristina.fintech.tokenization.TokenizationService;
import com.kristina.fintech.transaction.TransactionFactory;
import com.kristina.fintech.payment.PaymentRail;
import com.kristina.fintech.approval.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;

@RestController @RequestMapping("/api/payments")
public class PaymentController {
  private final TokenizationService tokenization; private final TransactionFactory factory; private final PaymentAuthorizationService authorization; private final ApprovalService approval;
  public PaymentController(TokenizationService tokenization,TransactionFactory factory,PaymentAuthorizationService authorization,ApprovalService approval){this.tokenization=tokenization;this.factory=factory;this.authorization=authorization;this.approval=approval;}
  @PostMapping("/tokenize") public ResponseEntity<TokenResponse> tokenize(@Valid @RequestBody TokenRequest r){ return ResponseEntity.ok(new TokenResponse(tokenization.tokenize(r.pan()))); }
  @PostMapping("/authorize") public ResponseEntity<PaymentResponse> authorize(@Valid @RequestBody PaymentRequest r){
    String token=tokenization.tokenize(r.pan());
    MonetaryAmount amount=new MonetaryAmount(new BigDecimal(r.amount()),r.currency());
    FiatPaymentTransaction tx=factory.create(amount,r.cardHolderName(),token);
    if(!tx.validateIngestionPerimeter()) return ResponseEntity.badRequest().body(new PaymentResponse("DENIED",null,tx.getTransactionId()));
    try { String paymentHash=authorization.authorize(tx, r.complianceDecision(), r.humanApproved()); return ResponseEntity.ok(new PaymentResponse("AUTHORIZED",paymentHash,tx.getTransactionId())); } catch (PaymentAuthorizationService.PaymentPolicyException e) { return ResponseEntity.status(403).body(new PaymentResponse(e.getMessage(),null,tx.getTransactionId())); }
  }
  public record TokenRequest(String pan){}
  public record TokenResponse(String token){}
  public record PaymentRequest(String pan,String cardHolderName,String amount,String currency,String complianceDecision,boolean humanApproved){}
  public record PaymentResponse(String status,String paymentReference,String transactionId){}
}
