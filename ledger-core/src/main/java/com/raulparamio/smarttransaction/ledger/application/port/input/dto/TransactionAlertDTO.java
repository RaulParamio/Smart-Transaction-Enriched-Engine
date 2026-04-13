package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionAlertDTO {
    private UUID transactionId;
    private BigDecimal amount;
    private String description;
    private String category;
    private BigDecimal fraudScore;
    private String aiJustification;
    private LocalDateTime createdAt;
}
