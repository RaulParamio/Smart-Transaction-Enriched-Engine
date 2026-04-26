package com.raulparamio.smarttransaction.ledger.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terminal {
    private  String id;
    private  String merchantName;
    private  UUID accountId;
    private  boolean active;


    // Lógica de Negocio: Ejemplo de validación en el dominio
    public void validateSupportForAmount(Double amount) {
        if (!active) {
            throw new IllegalStateException("El terminal " + id + " está desactivado.");
        }
        if (amount > 10000) {
            throw new IllegalArgumentException("El terminal no permite operaciones superiores a 10.000€.");
        }
    }
}