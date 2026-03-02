package com.raulparamio.smarttransaction.ledger.domain.port.input;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;

public interface CreateAccountUseCase {
    Account createAccount(Account account);
}
