package com.raulparamio.smarttransaction.ledger.application.service;


import com.raulparamio.smarttransaction.ledger.application.port.input.FindAccountUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindAccountService implements FindAccountUseCase {

        private final AccountRepositoryPort accountRepositoryPort;

        @Override
        public Account findByIban(String iban) {
            return accountRepositoryPort.findByIban(iban)
                    .orElseThrow(() -> new NoSuchElementException("No existe la cuenta con IBAN: " + iban));
        }

        @Override
        public Account findById(UUID id) {
            return accountRepositoryPort.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("No existe la cuenta con ID: " + id));
        }

    @Override
    public List<Account> execute() {
        return accountRepositoryPort.findAll();
    }

    }