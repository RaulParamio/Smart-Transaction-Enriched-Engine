package com.raulparamio.smarttransaction.ledger.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private UUID id;
    private String iban;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    public boolean hasEnoughBalance(BigDecimal amount) {
        // Devuelve true si el saldo es mayor o igual a la cantidad
        return this.balance.compareTo(amount) >= 0;
    }

    public void debit(BigDecimal amount) {
        if (!hasEnoughBalance(amount)) {
            throw new IllegalArgumentException("Saldo insuficiente para la operación");
        }
        if (this.status == AccountStatus.BLOCKED) {
            throw new IllegalStateException("La cuenta está bloqueada");
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void credit(BigDecimal amount) {
        if (this.status == AccountStatus.BLOCKED) {
            throw new IllegalStateException("La cuenta está bloqueada");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void blockAccount() {
        this.status = AccountStatus.BLOCKED;
        this.updatedAt = LocalDateTime.now();
    }
}