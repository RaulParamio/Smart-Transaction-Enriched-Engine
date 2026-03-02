package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // De Dominio a Base de Datos
    AccountEntity toEntity(Account account);

    // De Base de Datos a Dominio
    Account toDomain(AccountEntity accountEntity);
}
