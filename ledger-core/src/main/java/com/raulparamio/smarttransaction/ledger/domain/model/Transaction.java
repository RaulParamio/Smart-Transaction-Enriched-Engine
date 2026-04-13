package com.raulparamio.smarttransaction.ledger.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;


    // 1. AÑADIMOS SOLO EL ESTADO FINANCIERO
    private String status; // Ej: "COMPLETED", "REJECTED_BY_AI", "PENDING"

    // 2. EL MÉTODO AHORA ES MÁS SIMPLE (No sabe nada de la IA)
    public void markAsSuspicious() {
        this.status = "REJECTED_BY_AI"; // Cambiamos el estado para bloquear el dinero
    }

}

