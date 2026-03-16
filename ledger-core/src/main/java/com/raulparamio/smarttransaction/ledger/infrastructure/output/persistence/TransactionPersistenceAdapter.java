package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence;

import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionAnalysisEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaAccountRepository;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionAnalysisRepository;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {

    private final JpaTransactionRepository transactionRepository;
    private final JpaAccountRepository accountRepository;
    private final TransactionMapper transactionMapper; // Inyectado
    private final JpaTransactionAnalysisRepository analysisRepository;

    @Override
    public Transaction save(Transaction domainTx) {
        // 1. Recuperamos la entidad de la cuenta
        AccountEntity accountEntity = accountRepository.findById(domainTx.getAccountId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // 2. MapStruct convierte de Dominio a Entidad
        TransactionEntity entity = transactionMapper.toEntity(domainTx, accountEntity);

        // 3. Persistimos y capturamos la entidad resultante
        // ¡Aquí es donde JPA rellena el ID y la Fecha automáticamente!
        TransactionEntity savedEntity = transactionRepository.save(entity);

        // 4. Mapeamos la entidad "enriquecida" de vuelta al modelo de Dominio
        return transactionMapper.toDomain(savedEntity);
    }

    @Override
    public void saveAnalysis(TransactionAnalysis domainAnalysis) {
        // 1. Convertimos el análisis de dominio a entidad
        TransactionAnalysisEntity entity = transactionMapper.toAnalysisEntity(domainAnalysis);

        // 2. Guardamos físicamente en la tabla 'transaction_analysis'
        analysisRepository.save(entity);

        log.info("Análisis guardado correctamente para la transacción: {}", domainAnalysis.getTransactionId());
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        // Buscamos las entidades en la base de datos y las convertimos a dominio
        return transactionRepository.findByAccount_Id(accountId).stream()
                .map(transactionMapper::toDomain)
                .toList();
    }
}


