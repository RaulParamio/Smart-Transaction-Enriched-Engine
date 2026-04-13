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

    @Query("SELECT new com.raulparamio.smarttransaction.ledger.application.port.input.dto.CategoryStatDTO(" +
            "a.category, SUM(t.amount)) " +
            "FROM TransactionEntity t " +
            "JOIN TransactionAnalysisEntity a ON t.transactionId = a.transactionId " +
            "WHERE t.account.id = :accountId " +
            "GROUP BY a.category")

    List<CategoryStatDTO> findSpendingStats(@Param("accountId") UUID accountId);
    @Query("SELECT new com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionAlertDTO(" +
            "t.transactionId, t.amount, t.description, a.category, a.fraudScore, a.justification, t.createdAt) " +
            "FROM TransactionEntity t " +
            "JOIN TransactionAnalysisEntity a ON t.transactionId = a.transactionId " +
            "WHERE a.fraudScore >= :threshold " +
            "ORDER BY a.fraudScore DESC")
    List<TransactionAlertDTO> findHighRiskTransactions(@Param("threshold") BigDecimal threshold);
    // CAMBIO CLAVE: El tipo de retorno debe ser el DTO, no la Entity


    List<TransactionEntity> findByAccount_Id(UUID accountId);
}
