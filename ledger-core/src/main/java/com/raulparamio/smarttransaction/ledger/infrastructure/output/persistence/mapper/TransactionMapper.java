package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionCreateDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionDetailResponseDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionAnalysisEntity;
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

    // Ignoramos el ID y la fecha porque se generarán en la base de datos
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Transaction toDomain(TransactionCreateDTO dto);

    // 2. Mapeo para la tabla de IA (Entidad -> Dominio)
    TransactionAnalysis toAnalysisDomain(TransactionAnalysisEntity entity);

    @Mapping(target = "transactionId", source = "domain.transactionId")

    @Mapping(target = "transaction", ignore = true)
    TransactionAnalysisEntity toAnalysisEntity(TransactionAnalysis domain);

    // 3. El "Fusionador" (Multi-source mapping)
    // Combinamos los dos objetos de dominio en el DTO de salida
    @Mapping(target = "transactionId", source = "transaction.transactionId")
    @Mapping(target = "aiJustification", source = "analysis.justification")
    TransactionResponseDTO toResponseDTO(Transaction transaction, TransactionAnalysis analysis);


    @Mapping(target = "transactionId", source = "tx.transactionId")
    @Mapping(target = "aiJustification", source = "analysis.justification")
    @Mapping(target = "cleanDescription", source = "analysis.cleanDescription")
// Los campos con el mismo nombre (amount, category, etc.) se mapean solos
    TransactionDetailResponseDTO toDetailDTO(Transaction tx, TransactionAnalysis analysis);
}



