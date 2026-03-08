package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.application.port.input.ProcessTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessTransactionService implements ProcessTransactionUseCase {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;

    @Transactional
    @Override
    public void execute(UUID accountId, BigDecimal amount, String description) {
        // 1. Validar cuenta
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada"));

        // 2. Operación lógica (Resta saldo)
        account.debit(amount);
        accountRepository.save(account);

        // 3. Registrar el movimiento
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .accountId(accountId)
                .amount(amount)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }
}