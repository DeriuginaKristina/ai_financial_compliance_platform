package com.kristina.fintech;

import com.kristina.fintech.tokenization.TokenizationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenizationRegressionTest {
  @Test void panBecomesTokenAndTokenDiffers(){
    TokenizationService service=new TokenizationService("test-secret"); String pan="4111111111111111"; String token=service.tokenize(pan);
    assertNotEquals(pan,token); assertTrue(token.startsWith("tok_"));
  }
}
