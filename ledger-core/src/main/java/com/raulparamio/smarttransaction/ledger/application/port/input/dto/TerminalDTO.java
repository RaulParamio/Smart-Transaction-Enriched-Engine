package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TerminalDTO(
        @NotBlank(message = "El ID del terminal es obligatorio")
        String id, // Ej: "T-9988221"

        @NotBlank(message = "El nombre del comercio es obligatorio")
        String merchantName, // Ej: "MERCADONA MADRID"

        @NotNull(message = "El ID de la cuenta asociada es obligatorio")
        UUID accountId // El ID de la cuenta de Mercadona que creaste antes
) {}
