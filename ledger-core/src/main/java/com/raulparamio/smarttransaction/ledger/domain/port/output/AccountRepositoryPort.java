package com.raulparamio.smarttransaction.ledger.domain.port.output;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
    Account save(Account account);
    Optional<Account> findByIban(String iban);
    Optional<Account> findById(UUID id);
    void deleteById(UUID id);
}