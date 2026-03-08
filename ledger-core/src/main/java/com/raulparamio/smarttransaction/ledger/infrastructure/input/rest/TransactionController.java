package com.raulparamio.smarttransaction.ledger.infrastructure.input.rest;

import com.raulparamio.smarttransaction.ledger.application.port.input.ProcessTransactionUseCase;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ProcessTransactionUseCase processTransactionUseCase;
    // Aquí podrías añadir un FindTransactionUseCase más adelante para los GET

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody Transaction request) {
        // Ejecuta el flujo: Validar cuenta -> Restar Saldo -> Guardar Transacción
        processTransactionUseCase.execute(
                request.getAccountId(),
                request.getAmount(),
                request.getDescription()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Transacción registrada correctamente y saldo actualizado.");
    }
}
