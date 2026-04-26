package com.raulparamio.smarttransaction.ledger.application.port.output;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
    Account save(Account account);
    Optional<Account> findByIban(String iban);
    Optional<Account> findById(UUID id);
    Optional<Account> findByPhoneNumber(String phoneNumber);
    void deleteById(UUID id);
    List<Account> findAll();
    boolean existsById(UUID id);
}