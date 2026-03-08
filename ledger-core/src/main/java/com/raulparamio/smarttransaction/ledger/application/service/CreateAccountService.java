package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.application.port.input.CreateAccountUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public Account createAccount(Account account) {
        return accountRepositoryPort.save(account);
    }

}