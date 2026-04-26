package com.raulparamio.smarttransaction.ledger.application.port.output;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.CategoryStatDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionAlertDTO;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    void saveAnalysis(TransactionAnalysis analysis);
    List<Transaction> findAllByAccountId(UUID accountId);
    Optional<Transaction> findById(UUID id);
    Optional<TransactionAnalysis> findAnalysisById(UUID id);
    List<CategoryStatDTO> getStatsByAccountId(UUID accountId);
    List<TransactionAlertDTO> findHighRiskTransactions(BigDecimal highRiskThreshold);
}