package com.kristina.fintech.invoice;

import com.kristina.fintech.model.InvoiceData;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class InvoiceParser {
  public InvoiceData parse(String xml) {
    try {
      DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
      f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      f.setFeature("http://xml.org/sax/features/external-general-entities", false);
      f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      f.setXIncludeAware(false); f.setExpandEntityReferences(false); f.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); f.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      Document d = f.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
      return new InvoiceData(text(d,"ID"), text(d,"EndpointID"), text(d,"BuyerReference"), text(d,"IssueDate"), text(d,"DocumentCurrencyCode"), decimal(d,"PayableAmount"), decimal(d,"TaxAmount"));
    } catch (Exception e) { throw new IllegalArgumentException("Invoice XML rejected: " + e.getMessage()); }
  }
  private String text(Document d, String local) { NodeList n=d.getElementsByTagNameNS("*", local); if(n.getLength()==0)n=d.getElementsByTagName(local); return n.getLength()==0?null:n.item(0).getTextContent().trim(); }
  private BigDecimal decimal(Document d,String local){String s=text(d,local);return s==null?null:new BigDecimal(s);}
}
