package com.raulparamio.smarttransaction.ledger.application.port.input;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;

import java.util.List;
import java.util.UUID;

public interface FindAccountUseCase {
    Account findByIban(String iban);
    Account findById(UUID id);
    List<Account> execute();
}