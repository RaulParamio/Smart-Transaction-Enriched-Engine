package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CardPaymentDTO(
        @NotBlank(message = "El ID de la cuenta es obligatorio")
        String accountId,

        @NotBlank(message = "El texto en crudo del comercio no puede estar vacío")
        String rawMerchantText,

        @NotBlank(message = "El ID del terminal es obligatorio")
        String terminalId,

        @Positive(message = "El importe debe ser mayor que cero")
        BigDecimal amount,

        @NotBlank(message = "El modo de entrada (POS Entry Mode) es obligatorio")
        String posEntryMode // Ej: "05" Fisico (Chip), "07" Físico (Contactless), "81" Online (E-commerce)

) {
}
