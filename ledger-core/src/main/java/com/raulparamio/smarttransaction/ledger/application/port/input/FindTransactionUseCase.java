package com.raulparamio.smarttransaction.ledger.application.port.input;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.CategoryStatDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionAlertDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionDetailResponseDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import java.util.List;
import java.util.UUID;

public interface FindTransactionUseCase {
    List<TransactionResponseDTO> getTransactionsByAccountId(UUID accountId);
    TransactionDetailResponseDTO getTransactionDetail(UUID transactionId);
    List<CategoryStatDTO> getSpendingStatsByAccount(UUID accountId);
    List<TransactionAlertDTO> getFraudAlerts();
}
