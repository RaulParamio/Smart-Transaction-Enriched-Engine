package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.application.port.input.ProcessTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionCreateDTO;
import com.raulparamio.smarttransaction.ledger.application.port.output.AiAnalysisPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProcessTransactionService implements ProcessTransactionUseCase {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final AiAnalysisPort aiAnalysisPort;
    private final TransactionMapper mapper;


    @Transactional
    @Override
    public void execute(TransactionCreateDTO command) {
        // 1. Validar y obtener cuenta (usamos el ID que viene en el DTO)
        Account account = accountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada"));

        // 2. Operación lógica: Restar saldo
        account.debit(command.getAmount());
        accountRepository.save(account);

        // 3. Convertir DTO a Dominio usando el Mapper
        // Aquí el Mapper ignora el ID y la Fecha (se encarga la DB)
        Transaction transaction = mapper.toDomain(command);

        // 4. Registrar el movimiento
        // IMPORTANTE: Guardamos y capturamos el resultado para tener el ID generado por la DB
        Transaction savedTransaction = transactionRepository.save(transaction);

        // --- AQUÍ ENTRA LA MAGIA DE LA IA ---

        // 5. Mandamos a analizar la transacción real (la que ya tiene ID y Fecha)
        TransactionAnalysis analysis = aiAnalysisPort.analyze(savedTransaction);

        // 6. Enlazamos el análisis con el ID que la base de datos acaba de generar
        analysis.setTransactionId(savedTransaction.getTransactionId());

        // 7. Guardamos el análisis en su tabla separada
        transactionRepository.saveAnalysis(analysis);
    }
}