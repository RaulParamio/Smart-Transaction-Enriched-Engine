package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.application.port.input.DeleteAccountUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteAccountService implements DeleteAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public void deleteById(UUID id) {
        accountRepositoryPort.deleteById(id);
    }
}
