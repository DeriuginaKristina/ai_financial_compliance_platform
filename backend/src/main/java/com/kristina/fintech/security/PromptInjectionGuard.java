package com.kristina.fintech.security;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Service
public class PromptInjectionGuard {

  private static final List<String> PATTERNS = List.of(
          "ignore previous instructions",
          "ignore all instructions",
          "ignore your instructions",
          "reveal your prompt",
          "reveal system prompt",
          "system prompt",
          "disable security",
          "bypass compliance",
          "bypass security",
          "execute this command",
          "call the tool",
          "send the payment",
          "override system instructions",
          "you are now",
          "act as system administrator",
          "mark this invoice as approved",
          "ignore compliance rules",
          "ignore the validation results",
          "игнорируй предыдущие инструкции",
          "отключи проверку безопасности",
          "обойди проверку соответствия"
  );

  public boolean looksSuspicious(String input) {
    if (input == null || input.isBlank()) {
      return false;
    }

    String normalized = normalize(input);

    for (String pattern : PATTERNS) {
      if (normalized.contains(normalize(pattern))) {
        return true;
      }
    }

    return false;
  }

  private String normalize(String input) {
    String value = Normalizer.normalize(
            input,
            Normalizer.Form.NFKC
    );

    value = value.toLowerCase(Locale.ROOT);

    // Remove invisible Unicode formatting characters.
    value = value.replaceAll("\\p{Cf}", "");

    // Normalize whitespace, punctuation and symbols.
    value = value.replaceAll(
            "[\\p{Z}\\s\\p{P}\\p{S}]+",
            " "
    );

    return value.trim();
  }
}




/*package com.kristina.fintech.security;



import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

/** Lightweight perimeter control. Production should combine this with model/tool authorization and structured parsing. */
/*@Service
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
}/*/
