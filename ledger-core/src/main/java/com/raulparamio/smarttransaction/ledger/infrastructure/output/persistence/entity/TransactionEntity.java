package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", updatable = false, nullable = false)
    private UUID transactionId;

    @Column(name = "source_account_id", nullable = false)
    private UUID sourceAccountId; // Quien paga

    @Column(name = "destination_account_id", nullable = false)
    private UUID destinationAccountId; // Quien recibe

    private BigDecimal amount;
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private TransactionAnalysisEntity analysis;
}
