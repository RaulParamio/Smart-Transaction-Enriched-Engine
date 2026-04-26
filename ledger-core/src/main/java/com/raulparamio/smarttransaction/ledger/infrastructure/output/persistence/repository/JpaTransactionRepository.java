package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.CategoryStatDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionAlertDTO;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    // 1. Buscador de transacciones (Origen O Destino)
    @Query("SELECT t FROM TransactionEntity t WHERE t.sourceAccountId = :accountId OR t.destinationAccountId = :accountId ORDER BY t.createdAt DESC")
    List<TransactionEntity> findAllByAccountId(@Param("accountId") UUID accountId);

    // 2. Estadísticas de Gasto (Corregido 't.account.id' por 't.sourceAccountId')
    @Query("""
        SELECT new com.raulparamio.smarttransaction.ledger.application.port.input.dto.CategoryStatDTO(a.category, SUM(t.amount)) 
        FROM TransactionEntity t 
        JOIN TransactionAnalysisEntity a ON t.transactionId = a.transactionId 
        WHERE t.sourceAccountId = :accountId 
        GROUP BY a.category
    """)
    List<CategoryStatDTO> findSpendingStats(@Param("accountId") UUID accountId);

    // 3. Alertas de Fraude (Corregido 't.account.ownerName' por 't.sourceAccountId' o similar según tu DTO)
    @Query("""
    SELECT new com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionAlertDTO(
        t.transactionId, 
        t.amount, 
        t.description, 
        a.category, 
        a.fraudScore, 
        a.justification, 
        t.createdAt)
    FROM TransactionEntity t 
    JOIN TransactionAnalysisEntity a ON t.transactionId = a.transactionId 
    WHERE a.fraudScore >= :threshold
""")
    List<TransactionAlertDTO> findHighRiskTransactions(@Param("threshold") BigDecimal threshold);
}
