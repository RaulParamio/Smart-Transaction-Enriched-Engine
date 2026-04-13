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

    // =====================================================================
    // 1. MAPEOS DE LA TRANSACCIÓN FINANCIERA (CORE)
    // =====================================================================

    @Mapping(target = "account", source = "accountEntity")
    @Mapping(target = "transactionId", source = "domain.transactionId")
    @Mapping(target = "createdAt", source = "domain.createdAt")
    TransactionEntity toEntity(Transaction domain, AccountEntity accountEntity);

    @Mapping(target = "accountId", source = "entity.account.id")
    Transaction toDomain(TransactionEntity entity);

    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Transaction toDomain(TransactionCreateDTO dto);


    // =====================================================================
    // 2. MAPEOS DEL ANÁLISIS DE IA
    // =====================================================================

    TransactionAnalysis toAnalysisDomain(TransactionAnalysisEntity entity);

    // He arreglado el salto de línea que tenías aquí.
    // MapStruct es lo bastante listo para mapear transactionId automáticamente si se llaman igual,
    // pero si la Entidad tiene una relación @OneToOne (campo 'transaction'), lo ignoramos aquí.
    @Mapping(target = "transaction", ignore = true)
    TransactionAnalysisEntity toAnalysisEntity(TransactionAnalysis domain);


    // =====================================================================
    // 3. EL "FUSIONADOR" (RESPUESTAS HACIA EL FRONTEND )
    // =====================================================================

    @Mapping(target = "transactionId", source = "transaction.transactionId")
    @Mapping(target = "aiJustification", source = "analysis.justification")
    TransactionResponseDTO toResponseDTO(Transaction transaction, TransactionAnalysis analysis);

    @Mapping(target = "transactionId", source = "tx.transactionId")
    @Mapping(target = "aiJustification", source = "analysis.justification")
    @Mapping(target = "cleanDescription", source = "analysis.cleanDescription")
    TransactionDetailResponseDTO toDetailDTO(Transaction tx, TransactionAnalysis analysis);
}