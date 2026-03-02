package com.raulparamio.smarttransaction.ledger.infrastructure.input.rest;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.domain.port.input.CreateAccountUseCase;
import com.raulparamio.smarttransaction.ledger.domain.port.input.FindAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final FindAccountUseCase findAccountUseCase;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = createAccountUseCase.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/iban/{iban}")
    public ResponseEntity<Account> getAccount(@PathVariable String iban) {
        Account account = findAccountUseCase.findByIban(iban);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable UUID id) {
        return findAccountUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        findAccountUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}