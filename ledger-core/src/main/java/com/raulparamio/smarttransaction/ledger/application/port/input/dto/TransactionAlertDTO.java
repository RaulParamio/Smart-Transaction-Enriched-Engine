package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAlertDTO {
    private UUID transactionId;
    private BigDecimal amount;
    private String description;
    private String category;
    private BigDecimal fraudScore;
    private String aiJustification;
    private LocalDateTime createdAt;


    public TransactionAlertDTO(UUID transactionId, BigDecimal amount, BigDecimal fraudScore, String aiJustification) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.fraudScore = fraudScore;
        this.aiJustification = aiJustification;
    }
}
