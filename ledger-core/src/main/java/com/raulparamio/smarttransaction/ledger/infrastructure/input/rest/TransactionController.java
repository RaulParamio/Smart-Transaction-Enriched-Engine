package com.raulparamio.smarttransaction.ledger.infrastructure.input.rest;

import com.raulparamio.smarttransaction.ledger.application.port.input.FindTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.ProcessTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ProcessTransactionUseCase processTransactionUseCase;
    private final FindTransactionUseCase findTransactionUseCase;


    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionCreateDTO createDto) {
        // El controlador recibe el DTO minimalista
        processTransactionUseCase.execute(createDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(@RequestParam UUID accountId) {
        List<TransactionResponseDTO> transactions = findTransactionUseCase.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDetailResponseDTO> getTransactionDetail(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(findTransactionUseCase.getTransactionDetail(transactionId));
    }

    @GetMapping("/stats/{accountId}")
    public ResponseEntity<List<CategoryStatDTO>> getStats(@PathVariable UUID accountId) {
        return ResponseEntity.ok(findTransactionUseCase.getSpendingStatsByAccount(accountId));
    }

    @GetMapping("/admin/alerts")
    public ResponseEntity<List<TransactionAlertDTO>> getAlerts() {
        return ResponseEntity.ok(findTransactionUseCase.getFraudAlerts());
    }

}
