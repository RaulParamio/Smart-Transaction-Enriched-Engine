package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import com.raulparamio.smarttransaction.ledger.domain.model.AccountType;

import java.math.BigDecimal;

public record CreateAccountDTO(
        String iban,
        String ownerName,
        BigDecimal balance, // El saldo inicial que el banco le asigna
        String currency,
        String phoneNumber,
        AccountType accountType
) {}
