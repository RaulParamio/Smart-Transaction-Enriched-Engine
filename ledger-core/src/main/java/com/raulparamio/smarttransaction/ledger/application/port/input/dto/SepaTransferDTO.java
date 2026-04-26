package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record SepaTransferDTO(
        @NotBlank(message = "El ID de la cuenta origen es obligatorio")
        String accountId,

        @NotBlank(message = "El IBAN de destino es obligatorio")
        String targetIban,

        @NotBlank(message = "El nombre del beneficiario es obligatorio")
        String beneficiaryName,

        @NotBlank(message = "El concepto es obligatorio")
        String concept,

        @Positive(message = "El importe debe ser mayor que cero")
        BigDecimal amount
) {}