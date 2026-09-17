package com.kristina.fintech;

import com.kristina.fintech.invoice.InvoiceParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class XmlSecurityTest {
  @Test void doctypeIsRejected(){
    String xml="<?xml version=\"1.0\"?><!DOCTYPE foo [ <!ENTITY xxe SYSTEM \"file:///etc/passwd\"> ]><Invoice><ID>&xxe;</ID></Invoice>";
    assertThrows(IllegalArgumentException.class,()->new InvoiceParser().parse(xml));
  }
}
