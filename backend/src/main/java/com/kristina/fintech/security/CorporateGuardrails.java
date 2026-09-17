package com.kristina.fintech.security;

import com.kristina.fintech.model.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class CorporateGuardrails {
  private static final Logger log = LoggerFactory.getLogger(CorporateGuardrails.class);
  private static final Pattern PAN = Pattern.compile("\\b(?:\\d[ -]*?){13,19}\\b");
  private static final Pattern SECRET = Pattern.compile("(?i)(sk-[a-z0-9]{20,}|api[_-]?key\\s*[:=]\\s*\\S+|secret\\s*[:=]\\s*\\S+)");
  public boolean isSafe(TextChunk c) { if (c == null || c.content() == null) return false; if (SECRET.matcher(c.content()).find()) { log.warn("Security guardrail rejected unsafe context fragment"); return false; } return true; }
  public TextChunk sanitize(TextChunk c) { return new TextChunk(c.id(), PAN.matcher(c.content()).replaceAll("[REDACTED-PAYMENT-DATA]"), c.score()); }
}
