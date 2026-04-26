package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.CreateAccountDTO;
import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    // 1. Capa de Persistencia: Dominio <-> Entidad
    AccountEntity toEntity(Account account);
    Account toDomain(AccountEntity entity);

    // 2. Capa de Entrada: DTO -> Dominio
    // Forzamos el mapeo de balance por si acaso el DTO lo llama distinto internamente
    @Mapping(source = "balance", target = "balance")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toDomain(CreateAccountDTO dto);
}
