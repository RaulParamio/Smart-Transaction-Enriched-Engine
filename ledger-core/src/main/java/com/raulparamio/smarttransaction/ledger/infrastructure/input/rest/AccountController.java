package com.raulparamio.smarttransaction.ledger.infrastructure.input.rest;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.application.port.input.CreateAccountUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.DeleteAccountUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.FindAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final FindAccountUseCase findAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

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
        Account account = findAccountUseCase.findById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = findAccountUseCase.execute();

        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        deleteAccountUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}