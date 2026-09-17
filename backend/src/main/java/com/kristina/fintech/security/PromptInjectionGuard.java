package com.kristina.fintech.security;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

/** Lightweight perimeter control. Production should combine this with model/tool authorization and structured parsing. */
@Service
public class PromptInjectionGuard {
  private static final List<String> PATTERNS = List.of(
      "ignore previous instructions", "ignore all instructions", "system prompt",
      "reveal your prompt", "disable security", "bypass compliance", "execute this command",
      "call the tool", "send the payment");

  public boolean looksSuspicious(String input) {
    if (input == null) return false;
    String normalized = input.toLowerCase(Locale.ROOT);
    return PATTERNS.stream().anyMatch(normalized::contains);
  }
}
