package com.raulparamio.smarttransaction.ledger.application.service;

import com.raulparamio.smarttransaction.ledger.application.port.input.ProcessTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.*;
import com.raulparamio.smarttransaction.ledger.application.port.output.AiAnalysisPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.AccountRepositoryPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.TerminalRepositoryPort;
import com.raulparamio.smarttransaction.ledger.application.port.output.TransactionRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.*;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionAnalysisEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TerminalMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TransactionMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTerminalRepository;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTransactionAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessTransactionService implements ProcessTransactionUseCase {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final AiAnalysisPort aiAnalysisPort;
    private final TerminalRepositoryPort terminalRepositoryPort;
    private final TransactionMapper mapper;
    private final JpaTerminalRepository terminalRepository;
    private final TerminalMapper terminalMapper;
    private final JpaTransactionAnalysisRepository analysisRepository;

    @Override
    @Transactional
    public TransactionResponseDTO processCardPayment(CardPaymentDTO dto) {
        // 1. Origen: Buscamos al cliente (Usando el puerto/servicio de cuentas)
        Account origin = getAccountOrThrow(dto.accountId());

        // 2. Destino: Buscamos el Terminal a través del PUERTO
        // El puerto ya nos devuelve un objeto 'Terminal' de dominio, no una entidad

        Terminal terminal = terminalRepositoryPort.findById(dto.terminalId())
                .orElseThrow(() -> new RuntimeException("Error: Terminal " + dto.terminalId() + " no autorizado o no registrado"));

        // Buscamos la cuenta asociada al terminal
        Account destination = getAccountOrThrow(terminal.getAccountId().toString());

        // 3. Ejecutamos el movimiento de dinero
        executeMoneyTransfer(origin, destination, dto.amount());

        // 4. Mapeo del POS Entry Mode (ISO 8583)
        String entryMethod = switch (dto.posEntryMode()) {
            case "05" -> "CP (CHIP/EMV)";
            case "07" -> "CP (CONTACTLESS)";
            case "81" -> "CNP (E-COMMERCE/WEB)";
            default -> "MANUAL/UNKNOWN";
        };

        // 5. Creamos la transacción (Entidad de Dominio/Persistencia)
        Transaction tx = new Transaction();
        tx.setSourceAccountId(origin.getId());
        tx.setDestinationAccountId(destination.getId());
        tx.setAmount(dto.amount());
        tx.setCreatedAt(LocalDateTime.now());
        tx.setType(TransactionType.CARD_PAYMENT);

        String fullDescription = String.format("[%s] Mode: %s | Merchant: %s | TID: %s",
                entryMethod,
                dto.posEntryMode(),
                dto.rawMerchantText(),
                dto.terminalId());

        tx.setDescription(fullDescription);

        // 6. Analizamos con IA y guardamos
        return analyzeAndSave(tx);
    }


    @Override
    @Transactional
    public TransactionResponseDTO processBizum(BizumDTO dto) {
        Account origin = getAccountOrThrow(dto.accountId());
        Account destination = accountRepository.findByPhoneNumber(dto.targetPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Número no registrado"));

        executeMoneyTransfer(origin, destination, dto.amount());

        Transaction tx = new Transaction();
        tx.setSourceAccountId(origin.getId());
        tx.setDestinationAccountId(destination.getId());
        tx.setAmount(dto.amount());
        tx.setCreatedAt(LocalDateTime.now());
        tx.setType(TransactionType.BIZUM);
        tx.setDescription("Bizum de " + origin.getOwnerName() + ": " + dto.concept());

        return analyzeAndSave(tx);
    }

    @Override
    @Transactional
    public TransactionResponseDTO processTransfer(SepaTransferDTO dto) {
        Account origin = getAccountOrThrow(dto.accountId());

        // Destino: Buscamos por IBAN
        Account destination = accountRepository.findByIban(dto.targetIban())
                .orElseThrow(() -> new RuntimeException("Error: IBAN de destino no encontrado"));

        executeMoneyTransfer(origin, destination, dto.amount());

        Transaction tx = new Transaction();
        tx.setSourceAccountId(origin.getId());
        tx.setDestinationAccountId(destination.getId());
        tx.setAmount(dto.amount());
        tx.setCreatedAt(LocalDateTime.now());
        tx.setType(TransactionType.SEPA_TRANSFER);
        tx.setDescription("TRANSFERENCIA A " + dto.beneficiaryName() + " CONCEPTO: " + dto.concept());
        tx.setReference(dto.targetIban());

        return analyzeAndSave(tx);
    }

    // ==========================================
    // ⚙️ MÉTODOS PRIVADOS DE APOYO (Lógica Contable)
    // ==========================================

    private Account getAccountOrThrow(String accountId) {
        return accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new RuntimeException("Error: La cuenta " + accountId + " no existe."));
    }

    private void executeMoneyTransfer(Account origin, Account destination, BigDecimal amount) {
        // Usamos los métodos de tu entidad Account (Modelo de Dominio Rico)
        origin.debit(amount);
        destination.credit(amount);

        // Persistimos los nuevos saldos
        accountRepository.save(origin);
        accountRepository.save(destination);
    }

    private TransactionResponseDTO analyzeAndSave(Transaction tx) {
        Transaction savedTx = transactionRepository.save(tx);
        TransactionAnalysis analysis = aiAnalysisPort.analyze(savedTx);
        analysis.setTransactionId(savedTx.getTransactionId());

        TransactionAnalysisEntity entityToSave = mapper.toAnalysisEntity(analysis);
        analysisRepository.save(entityToSave);

        return mapper.toResponseDTO(savedTx, analysis);
    }
}