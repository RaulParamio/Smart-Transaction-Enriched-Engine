package com.raulparamio.smarttransaction.ledger.application.port.output;

import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import java.util.UUID;

public interface TransactionRepositoryPort {
    void save(Transaction transaction);
    void saveAnalysis(UUID transactionId, TransactionAnalysis analysis);
}