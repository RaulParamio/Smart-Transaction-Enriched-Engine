package com.raulparamio.smarttransaction.ledger.domain.port.input;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface FindAccountUseCase {
    Account findByIban(String iban);
    Optional<Account> findById(UUID id);
    void deleteById(UUID id);
}