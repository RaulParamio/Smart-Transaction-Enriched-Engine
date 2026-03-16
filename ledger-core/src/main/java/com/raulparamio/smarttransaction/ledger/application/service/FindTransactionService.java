package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.application.port.input.FindTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTransactionService implements FindTransactionUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final JpaTransactionAnalysisRepository analysisRepository;
    private final TransactionMapper mapper; // MapStruct

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByAccountId(UUID accountId) {

        // 1. Recuperamos la base financiera
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        // 2. Enriquecemos cada transacción con su análisis
        return transactions.stream().map(tx -> {
            // Buscamos el análisis en la tabla de IA
            TransactionAnalysis analysis = analysisRepository.findById(tx.getTransactionId())
                    .map(mapper::toAnalysisDomain)
                    .orElse(null);

            // Fusionamos usando MapStruct
            return mapper.toResponseDTO(tx, analysis);
        }).toList();
    }
}