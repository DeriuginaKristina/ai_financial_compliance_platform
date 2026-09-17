package com.kristina.fintech.model;

import java.math.BigDecimal;
public record InvoiceData(String invoiceNumber, String supplierId, String buyerId, String issueDate, String currency, BigDecimal payableAmount, BigDecimal taxTotal) {}
