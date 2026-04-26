package com.raulparamio.smarttransaction.ledger.application.port.output;

import com.raulparamio.smarttransaction.ledger.domain.model.Terminal;

import java.util.List;
import java.util.Optional;

public interface TerminalRepositoryPort {
    Optional<Terminal> findById(String id);
    void save(Terminal terminal);
    List<Terminal> findAll();
}