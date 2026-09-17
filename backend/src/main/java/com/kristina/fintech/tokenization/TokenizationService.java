package com.kristina.fintech.tokenization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac; import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets; import java.util.HexFormat;

@Service
public class TokenizationService {
  private final byte[] secret;
  public TokenizationService(@Value("${app.tokenization.secret}") String secret){this.secret=secret.getBytes(StandardCharsets.UTF_8);}
  public String tokenize(String pan){
    if(pan==null || !pan.matches("\\d{12,19}")) throw new IllegalArgumentException("Invalid PAN perimeter");
    try { Mac mac=Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret,"HmacSHA256")); String digest=HexFormat.of().formatHex(mac.doFinal(pan.getBytes(StandardCharsets.UTF_8))); return "tok_"+digest.substring(0,24); }
    catch(Exception e){throw new IllegalStateException("Tokenization unavailable",e);}
  }
}
