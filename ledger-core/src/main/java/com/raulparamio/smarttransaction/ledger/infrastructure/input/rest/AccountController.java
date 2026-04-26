package com.raulparamio.smarttransaction.ledger.infrastructure.input.rest;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.CreateAccountDTO;
import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.application.port.input.CreateAccountUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.DeleteAccountUseCase;
import com.raulparamio.smarttransaction.ledger.application.port.input.FindAccountUseCase;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.AccountMapper;
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
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountDTO dto) {
        // 2. Usamos el mapper inyectado para convertir DTO -> Dominio
        Account accountRequest = accountMapper.toDomain(dto);
        // 3. Llamamos al CASO DE USO (createAccountUseCase), no al servicio directamente
        Account savedAccount = createAccountUseCase.createAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
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