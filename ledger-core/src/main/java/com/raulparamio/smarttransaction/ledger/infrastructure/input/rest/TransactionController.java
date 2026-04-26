package com.raulparamio.smarttransaction.ledger.infrastructure.input.rest;

import com.raulparamio.smarttransaction.ledger.application.port.input.FindTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.ProcessTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    // Asegúrate de que estos nombres coincidan con tus @Service o @Component
    private final ProcessTransactionUseCase processTransactionUseCase;
    private final FindTransactionUseCase findTransactionUseCase;

    @PostMapping("/card")
    public ResponseEntity<TransactionResponseDTO> receiveCardPayment(@Valid @RequestBody CardPaymentDTO dto) {
        return ResponseEntity.ok(processTransactionUseCase.processCardPayment(dto));
    }

    @PostMapping("/bizum")
    public ResponseEntity<TransactionResponseDTO> receiveBizum(@Valid @RequestBody BizumDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(processTransactionUseCase.processBizum(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> receiveTransfer(@Valid @RequestBody SepaTransferDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(processTransactionUseCase.processTransfer(request));
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
