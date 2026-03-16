package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class TransactionResponseDTO {
    UUID transactionId;
    BigDecimal amount;
    String description;
    LocalDateTime createdAt;

    // Datos de IA (Vienen de otro dominio pero se unifican aquí)
    String category;
    Double fraudScore;
    String aiJustification;
}