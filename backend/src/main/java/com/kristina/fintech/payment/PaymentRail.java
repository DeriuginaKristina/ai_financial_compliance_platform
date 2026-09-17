package com.kristina.fintech.payment;
import com.kristina.fintech.model.FinancialTransaction;
public interface PaymentRail { String execute(FinancialTransaction transaction); }
