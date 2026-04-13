package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.application.port.input.ProcessTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionCreateDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import com.raulparamio.smarttransaction.ledger.application.port.output.AiAnalysisPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionAnalysisEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProcessTransactionService implements ProcessTransactionUseCase {

    private final AccountRepositoryPort accountRepository; // ¿Tienes esto declarado en otro sitio?
    private final TransactionRepositoryPort transactionRepository;
    private final AiAnalysisPort aiAnalysisPort;
    private final TransactionMapper mapper;
    private final JpaTransactionAnalysisRepository analysisRepository;

    @Transactional
    @Override
    public TransactionResponseDTO execute(TransactionCreateDTO command) {

        // 3. RECUPERAMOS LA PARTE PERDIDA: Crear la transacción
        // (Asumo que aquí hacías lo de buscar la cuenta y restar saldo)
        Transaction transaction = mapper.toDomain(command);
        Transaction savedTx = transactionRepository.save(transaction);

        // --- EMPIEZA LA IA ---

        TransactionAnalysis analysis = aiAnalysisPort.analyze(savedTx);
        analysis.setTransactionId(savedTx.getTransactionId());

        TransactionAnalysisEntity entityToSave = mapper.toAnalysisEntity(analysis);
        analysisRepository.save(entityToSave);

        // Devolvemos la respuesta
        return mapper.toResponseDTO(savedTx, analysis);
    }
}