package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence;

import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaAccountRepository;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {

    private final JpaTransactionRepository transactionRepository;
    private final JpaAccountRepository accountRepository;
    private final TransactionMapper transactionMapper; // Inyectado

    @Override
    public void save(Transaction domainTx) {
        // 1. Recuperamos la entidad de la cuenta (obligatorio para la FK)
        AccountEntity accountEntity = accountRepository.findById(domainTx.getAccountId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // 2. MapStruct hace to do el trabajo sucio aquí
        TransactionEntity entity = transactionMapper.toEntity(domainTx, accountEntity);

        // 3. Persistimos
        transactionRepository.save(entity);
    }

    @Override
    public void saveAnalysis(UUID transactionId, TransactionAnalysis domainAnalysis) {
        // Por ahora lo dejamos vacío para que compile.
        // En el Hito 2 (IA), aquí llamaremos al Mapper y al JpaTransactionAnalysisRepository.
        log.info("Simulando guardado de análisis para la transacción ID: {}", transactionId);
    }
}


