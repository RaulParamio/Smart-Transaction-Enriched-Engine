package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.application.port.input.FindTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.CategoryStatDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionAlertDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionDetailResponseDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTransactionService implements FindTransactionUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final JpaTransactionAnalysisRepository analysisRepository;
    private final AccountRepositoryPort accountRepository;
    private final TransactionMapper mapper; // MapStruct

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByAccountId(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findAllByAccountId(accountId);
        // 2. Enriquecemos cada transacción con su análisis
        return transactions.stream().map(tx -> {
            // Buscamos el análisis por el ID de la transacción
            TransactionAnalysis analysis = transactionRepository.findAnalysisById(tx.getTransactionId())
                    .orElse(null);
            // Fusionamos usando MapStruct
            return mapper.toResponseDTO(tx, analysis);
        }).toList();
    }


    @Override
    public TransactionDetailResponseDTO getTransactionDetail(UUID transactionId) {
        // 1. Buscamos la transacción financiera
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("No existe la transacción: " + transactionId));

        // 2. Buscamos el análisis. Si no existe (ej: la IA falló), devolvemos uno vacío
        TransactionAnalysis analysis = transactionRepository.findAnalysisById(transactionId)
                .orElse(TransactionAnalysis.builder()
                        .category("DESCONOCIDA")
                        .justification("Análisis no disponible en este momento.")
                        .build());

        // 3. Mapeamos el "collage" al DTO final
        return mapper.toDetailDTO(transaction, analysis);
    }

    @Override
    public List<CategoryStatDTO> getSpendingStatsByAccount(UUID accountId) {
        // Verificamos si la cuenta existe antes de pedir estadísticas
        if (!accountRepository.existsById(accountId)) {
            throw new NoSuchElementException("Cuenta no encontrada");
        }
        return transactionRepository.getStatsByAccountId(accountId);
    }

    @Override
    public List<TransactionAlertDTO> getFraudAlerts() {
        BigDecimal highRiskThreshold = new BigDecimal("0.8"); // Podría venir de un archivo .properties
        return transactionRepository.findHighRiskTransactions(highRiskThreshold);
    }
}