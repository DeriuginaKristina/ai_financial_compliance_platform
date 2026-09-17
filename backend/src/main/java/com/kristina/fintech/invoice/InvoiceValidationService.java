package com.kristina.fintech.invoice;

import com.kristina.fintech.model.InvoiceData;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceValidationService {
  public List<String> validate(InvoiceData i) {
    List<String> errors=new ArrayList<>();
    if (blank(i.invoiceNumber())) errors.add("invoiceNumber missing");
    if (blank(i.currency())) errors.add("currency missing");
    if (i.payableAmount()==null || i.payableAmount().signum()<=0) errors.add("payableAmount must be positive");
    if (i.taxTotal()==null || i.taxTotal().signum()<0) errors.add("taxTotal missing or negative");
    if (blank(i.issueDate())) errors.add("issueDate missing");
    return errors;
  }
  private boolean blank(String s){return s==null||s.isBlank();}
}
