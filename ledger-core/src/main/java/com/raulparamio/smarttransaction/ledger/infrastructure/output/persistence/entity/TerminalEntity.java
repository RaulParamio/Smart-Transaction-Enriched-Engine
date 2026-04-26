package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "terminals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TerminalEntity {

    @Id
    private String id; // El ID que viene en el DTO (ej: "T-9988221")

    @Column(nullable = false)
    private String merchantName; // Para tener el nombre legible (ej: "Mercadona Madrid")

    @Column(name = "account_id", nullable = false)
    private UUID accountId; // El ID de la cuenta donde se ingresará el dinero
}
