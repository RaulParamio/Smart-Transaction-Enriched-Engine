package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transaction_analysis")
@Data
public class TransactionAnalysisEntity {
    @Id
    @Column(name = "transaction_id")
    private UUID transactionId;

    @OneToOne
    @JoinColumn(name = "transaction_id", insertable = false, updatable = false)
    private TransactionEntity transaction;

    private String cleanDescription;
    private String category;
    private BigDecimal fraudScore;
    private String justification;
}
