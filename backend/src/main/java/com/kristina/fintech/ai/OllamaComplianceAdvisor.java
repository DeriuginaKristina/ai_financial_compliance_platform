package com.kristina.fintech.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kristina.fintech.model.ComplianceAiAssessment;
import com.kristina.fintech.model.InvoiceData;
import com.kristina.fintech.security.PromptInjectionGuard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.time.Duration;
import java.util.List;

@Service
public class OllamaComplianceAdvisor {
  private final RestClient client; private final String model; private final ObjectMapper mapper = new ObjectMapper(); private final PromptInjectionGuard guard;
  public OllamaComplianceAdvisor(@Value("${app.ollama.base-url}") String baseUrl, @Value("${app.ollama.model}") String model, PromptInjectionGuard guard){
    this.client=RestClient.builder().baseUrl(baseUrl).requestFactory(new org.springframework.http.client.SimpleClientHttpRequestFactory() {{ setConnectTimeout(Duration.ofSeconds(2)); setReadTimeout(Duration.ofSeconds(10)); }}).build();
    this.model=model; this.guard=guard;
  }

  public ComplianceAiAssessment assess(InvoiceData invoice, List<String> errors, String context){
    boolean suspicious = guard.looksSuspicious(context);
    if (!errors.isEmpty()) return new ComplianceAiAssessment("DETERMINISTIC_VALIDATION_FAILED", "HIGH", model, suspicious);
    if (suspicious) return new ComplianceAiAssessment("SUSPICIOUS_INSTRUCTION_PATTERN_DETECTED", "LOW", model, true);
    try {
      String prompt = "You are a compliance advisor. Return JSON only with keys riskNote, confidence. Do not invent legal requirements. Never authorize payments. Invoice="+invoice+" Evidence="+context;
      String body = mapper.createObjectNode().put("model", model).put("prompt", prompt).put("stream", false).toString();
      String response=client.post().uri("/api/generate").body(body).header("Content-Type","application/json").retrieve().body(String.class);
      JsonNode n=mapper.readTree(response);
      String text=n.path("response").asText("");
      try {
        JsonNode structured=mapper.readTree(text);
        return new ComplianceAiAssessment(structured.path("riskNote").asText("LOCAL_AI_NO_RISK_NOTE"), structured.path("confidence").asText("UNKNOWN"), model, false);
      } catch(Exception ignored) {
        return new ComplianceAiAssessment("UNSTRUCTURED_AI_OUTPUT_REJECTED", "LOW", model, false);
      }
    } catch(Exception e){ return new ComplianceAiAssessment("LOCAL_AI_UNAVAILABLE", "UNKNOWN", model, false); }
  }
}
