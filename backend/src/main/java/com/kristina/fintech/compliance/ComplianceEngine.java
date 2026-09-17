package com.kristina.fintech.compliance;

import com.kristina.fintech.ai.OllamaComplianceAdvisor;
import com.kristina.fintech.model.*;
import com.kristina.fintech.retrieval.HybridRetrievalService;
import com.kristina.fintech.invoice.InvoiceValidationService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComplianceEngine {
  private final InvoiceValidationService validation; private final HybridRetrievalService retrieval; private final OllamaComplianceAdvisor ai;
  public ComplianceEngine(InvoiceValidationService validation, HybridRetrievalService retrieval, OllamaComplianceAdvisor ai){this.validation=validation;this.retrieval=retrieval;this.ai=ai;}
  public Draft evaluate(InvoiceData invoice, String hash, String txId){
    List<String> errors=validation.validate(invoice); List<TextChunk> evidence=retrieval.retrieve((invoice.invoiceNumber()==null?"":invoice.invoiceNumber())+" "+(invoice.currency()==null?"":invoice.currency()));
    var aiAssessment=ai.assess(invoice, errors, evidence.stream().map(TextChunk::content).reduce("",(a,b)->a+" | "+b));
    String note=aiAssessment.riskNote();
    if(!errors.isEmpty()) return new Draft(ComplianceDecision.DENY,RiskLevel.HIGH,90,"Deterministic invoice validation failed: "+String.join(", ",errors),"2026.09",evidence,hash,txId,note);
    if(aiAssessment.suspiciousInput()) return new Draft(ComplianceDecision.DENY,RiskLevel.HIGH,95,"Suspicious instruction pattern detected in compliance context", "2026.09",evidence,hash,txId,note);
    if(evidence.isEmpty()) return new Draft(ComplianceDecision.REVIEW,RiskLevel.MEDIUM,55,"No sufficiently relevant compliance evidence was retrieved", "2026.09",evidence,hash,txId,note);
    return new Draft(ComplianceDecision.APPROVE,RiskLevel.LOW,10,"Invoice passed deterministic checks; local AI supplied advisory context", "2026.09",evidence,hash,txId,note);
  }
  public record Draft(ComplianceDecision decision,RiskLevel riskLevel,int riskScore,String reason,String ruleVersion,List<TextChunk> evidence,String invoiceHash,String transactionId,String aiNote){}
}
