package com.kristina.fintech.ledger;

import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;

/** Web3j-ready cryptographic adapter. External RPC submission is intentionally not automatic in the MVP. */
@Component
public class Web3AuditAnchor {
  public String keccak256(String sha256Hex){return Hash.sha3(sha256Hex);}
}
