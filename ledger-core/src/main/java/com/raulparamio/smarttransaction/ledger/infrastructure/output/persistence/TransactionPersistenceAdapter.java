package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.CategoryStatDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionAlertDTO;
import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionAnalysisEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaAccountRepository;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionAnalysisRepository;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
        // 1. Convertimos de Dominio a Entidad directamente
        // Ahora que TransactionEntity usa UUIDs, no necesitamos buscar la AccountEntity primero
        TransactionEntity entity = transactionMapper.toEntity(domainTx);

        // 2. Persistimos en la base de datos
        TransactionEntity savedEntity = transactionRepository.save(entity);

        // 3. Devolvemos al Dominio con el ID generado por la DB
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
    public List<Transaction> findAllByAccountId(UUID accountId) {
        // 1. Llamamos al nuevo métiodo del repositorio que busca en Origen O Destino
        List<TransactionEntity> entities = transactionRepository.findAllByAccountId(accountId);

        // 2. Convertimos la lista de entidades a lista de objetos de dominio
        return entities.stream()
                .map(transactionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        // 1. Buscamos en la tabla de transacciones usando el JpaRepository
        // 2. Si lo encuentra, usamos el mapper para pasar de Entity a Domain
        return transactionRepository.findById(id)
                .map(transactionMapper::toDomain);
    }

    @Override
    public Optional<TransactionAnalysis> findAnalysisById(UUID id) {
        // 1. Buscamos en la tabla de análisis
        // 2. Usamos el mapper específico para análisis
        return analysisRepository.findById(id)
                .map(transactionMapper::toAnalysisDomain);
    }

    @Override
    public List<CategoryStatDTO> getStatsByAccountId(UUID accountId) {
        return transactionRepository.findSpendingStats(accountId);
    }

    @Override
    public List<TransactionAlertDTO> findHighRiskTransactions(BigDecimal threshold) {
        return transactionRepository.findHighRiskTransactions(threshold);
    }
}


