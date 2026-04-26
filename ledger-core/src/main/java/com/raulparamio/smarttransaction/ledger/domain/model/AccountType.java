package com.raulparamio.smarttransaction.ledger.domain.model;

public enum AccountType {
    PERSONAL,  // Usuarios normales
    BUSINESS,  // Mercadona, Amazon, etc.
    SYSTEM     // Cuentas técnicas del banco (para liquidaciones)
}