package com.raulparamio.smarttransaction.ledger.application.port.input;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionCreateDTO;
import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionResponseDTO;
import org.springframework.transaction.annotation.Transactional;



public interface ProcessTransactionUseCase {
    @Transactional
    TransactionResponseDTO execute(TransactionCreateDTO command);


}
