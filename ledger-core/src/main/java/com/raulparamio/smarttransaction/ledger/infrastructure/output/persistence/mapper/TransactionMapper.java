package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper;

import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Pasamos de Dominio a Entidad
    // Usamos @Mapping para decirle que el objeto AccountEntity completo va al campo 'account'
    @Mapping(target = "account", source = "accountEntity")
    @Mapping(target = "transactionId", source = "domain.transactionId")
    @Mapping(target = "createdAt", source = "domain.createdAt")
    TransactionEntity toEntity(Transaction domain, AccountEntity accountEntity);

    // Pasamos de Entidad a Dominio
    @Mapping(target = "accountId", source = "entity.account.id")
    Transaction toDomain(TransactionEntity entity);
}
