package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper;

import com.raulparamio.smarttransaction.ledger.domain.model.Terminal;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TerminalEntity;
import org.springframework.stereotype.Component;

@Component
public class TerminalMapper {

    public Terminal toDomain(TerminalEntity entity) {
        if (entity == null) return null;
        return new Terminal(
                entity.getId(),
                entity.getMerchantName(),
                entity.getAccountId(),
                true // O el campo que tengas en la entidad
        );
    }

    public TerminalEntity toEntity(Terminal domain) {
        if (domain == null) return null;
        return new TerminalEntity(
                domain.getId(),
                domain.getMerchantName(),
                domain.getAccountId()
        );
    }
}