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
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
    private UUID destinationAccountId;
    private UUID sourceAccountId;

    //  LOS DIFERENTES PAGOS
    private TransactionType type; // BIZUM, CARD_PAYMENT, SEPA_TRANSFER
    private String reference;     // Guardará el teléfono, el IBAN o el ID del datáfono

    // 2. INICIALIZAMOS EL ESTADO POR DEFECTO
    @Builder.Default
    private String status = "PENDING"; // Así toda transacción nace pendiente

    public void markAsSuspicious() {
        this.status = "REJECTED_BY_AI";
    }

}