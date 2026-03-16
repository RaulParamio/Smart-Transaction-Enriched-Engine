package com.raulparamio.smarttransaction.ledger.application.port.output;

import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;

import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    void saveAnalysis(TransactionAnalysis analysis);
    List<Transaction> findByAccountId(UUID accountId);
}