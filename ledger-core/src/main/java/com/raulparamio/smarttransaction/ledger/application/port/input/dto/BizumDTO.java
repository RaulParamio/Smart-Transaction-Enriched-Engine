package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BizumDTO(
        @NotBlank(message = "El ID de la cuenta origen es obligatorio")
        String accountId,

        @NotBlank(message = "El teléfono de destino es obligatorio")
        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Formato de teléfono inválido")
        String targetPhoneNumber,

        @NotBlank(message = "El concepto del Bizum es obligatorio")
        String concept, // Ej: "Cena pizzería" o "Regalo cumple"

        @Positive(message = "El importe debe ser mayor que cero")
        BigDecimal amount
) {}