package com.raulparamio.smarttransaction.ledger.application.port.input;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProcessTransactionUseCase {
    void execute(UUID accountId, BigDecimal amount, String description);
}
