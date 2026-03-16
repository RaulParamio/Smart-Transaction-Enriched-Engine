package com.raulparamio.smarttransaction.ledger.application.port.input;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import java.util.List;
import java.util.UUID;

public interface FindTransactionUseCase {
    List<TransactionResponseDTO> getTransactionsByAccountId(UUID accountId);
}
