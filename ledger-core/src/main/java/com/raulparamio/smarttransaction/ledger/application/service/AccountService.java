package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.domain.port.input.CreateAccountUseCase;
import com.raulparamio.smarttransaction.ledger.domain.port.input.FindAccountUseCase;
import com.raulparamio.smarttransaction.ledger.domain.port.output.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, FindAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public Account createAccount(Account account) {
        return accountRepositoryPort.save(account);
    }

    @Override
    public Account findByIban(String iban) {
        // Buscamos en BBDD. Si no existe, lanzamos un error básico.
        return accountRepositoryPort.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con IBAN: " + iban));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountRepositoryPort.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        accountRepositoryPort.deleteById(id);
    }
}