package com.raulparamio.smarttransaction.ledger.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Transaction {
    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
    private TransactionAnalysis analysis;
}