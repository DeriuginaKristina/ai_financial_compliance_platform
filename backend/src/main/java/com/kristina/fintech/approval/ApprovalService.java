package com.kristina.fintech.approval;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {
  private final Set<String> approved = ConcurrentHashMap.newKeySet();
  public void humanApprove(String txId) { if(txId==null||txId.isBlank()) throw new IllegalArgumentException("transactionId required"); approved.add(txId); }
  public boolean isApproved(String txId) { return approved.contains(txId); }
}
