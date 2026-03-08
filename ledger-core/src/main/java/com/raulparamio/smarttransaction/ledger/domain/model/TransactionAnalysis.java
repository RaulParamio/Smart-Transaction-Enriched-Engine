package com.raulparamio.smarttransaction.ledger.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionAnalysis {
    private String cleanDescription;
    private String category;
    private BigDecimal fraudScore;
    private String justification;
}
