package com.raulparamio.smarttransaction.ledger.infrastructure.input.rest;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TerminalDTO;
import com.raulparamio.smarttransaction.ledger.application.port.output.TerminalRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Terminal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terminals")
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalRepositoryPort terminalRepositoryPort;


    @PostMapping
    public ResponseEntity<Void> createTerminal(@Valid @RequestBody TerminalDTO dto) {
        // Convertimos el DTO al modelo de dominio directamente para guardar
        Terminal terminal = new Terminal(
                dto.id(),
                dto.merchantName(),
                dto.accountId(),
                true // activo por defecto
        );

        terminalRepositoryPort.save(terminal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<TerminalDTO>> getAllTerminals() {
        List<TerminalDTO> terminals = terminalRepositoryPort.findAll()
                .stream()
                .map(terminal -> new TerminalDTO(
                        terminal.getId(),
                        terminal.getMerchantName(),
                        terminal.getAccountId()
                ))
                .toList();

        return ResponseEntity.ok(terminals);
    }
}
