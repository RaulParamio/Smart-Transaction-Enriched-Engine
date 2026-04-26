package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionCreateDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionDetailResponseDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionAnalysisEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // 1. Core: Ahora que ambos se llaman transactionId, no hace falta @Mapping manual
    TransactionEntity toEntity(Transaction domain);

    Transaction toDomain(TransactionEntity entity);

    // 2. Entrada DTO -> Dominio
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "sourceAccountId", source = "accountId")
    @Mapping(target = "status", ignore = true) // Ignoramos porque el dominio lo pone a PENDING por defecto
    @Mapping(target = "type", ignore = true) // Se asigna en el Service según el método
    @Mapping(target = "reference", ignore = true)
    Transaction toDomain(TransactionCreateDTO dto);

    // En TransactionMapper.java

    @Mapping(target = "transactionId", source = "transaction.transactionId") // Cambiado 'tx' por 'transaction'
    @Mapping(target = "aiJustification", source = "analysis.justification")
    @Mapping(target = "cleanDescription", source = "analysis.cleanDescription")
// Si el DTO tiene campos que se llaman igual en 'transaction', MapStruct los mapeará solo
    TransactionDetailResponseDTO toDetailDTO(Transaction transaction, TransactionAnalysis analysis);

    // 3. IA y Respuestas
    TransactionAnalysis toAnalysisDomain(TransactionAnalysisEntity entity);

    @Mapping(target = "transaction", ignore = true)
    TransactionAnalysisEntity toAnalysisEntity(TransactionAnalysis domain);

    @Mapping(target = "transactionId", source = "tx.transactionId")
    @Mapping(target = "amount", source = "tx.amount")
    @Mapping(target = "description", source = "tx.description")
    @Mapping(target = "createdAt", source = "tx.createdAt")
    // Campos que vienen del Análisis de IA
    @Mapping(target = "category", source = "analysis.category")
    @Mapping(target = "aiJustification", source = "analysis.justification")
    // El porcentaje SOLO para el fraudScore
    @Mapping(target = "fraudScore", expression = "java(formatPercentage(analysis.getFraudScore()))")
    TransactionResponseDTO toResponseDTO(Transaction tx, TransactionAnalysis analysis);

    default String formatPercentage(BigDecimal score) {
        if (score == null) return "0%";
        return (int)(score.doubleValue() * 100) + "%";
    }
}