package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence;

import com.raulparamio.smarttransaction.ledger.application.port.output.TerminalRepositoryPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Terminal;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.TerminalMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaTerminalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TerminalPersistenceAdapter implements TerminalRepositoryPort {

    private final JpaTerminalRepository jpaRepository;
    private final TerminalMapper mapper;

    @Override
    public Optional<Terminal> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void save(Terminal terminal) {
        jpaRepository.save(mapper.toEntity(terminal));
    }

    @Override
    public List<Terminal> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
