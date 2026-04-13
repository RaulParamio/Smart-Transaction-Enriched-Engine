package com.raulparamio.smarttransaction.ledger.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionAnalysis {
    private UUID transactionId; //(FK)
    private String cleanDescription;
    private String category;
    private BigDecimal fraudScore;
    private String justification;

}
