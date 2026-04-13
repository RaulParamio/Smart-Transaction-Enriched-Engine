package com.raulparamio.smarttransaction.ledger.application.port.input.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryStatDTO {
    private String category;
    private BigDecimal totalAmount;
}