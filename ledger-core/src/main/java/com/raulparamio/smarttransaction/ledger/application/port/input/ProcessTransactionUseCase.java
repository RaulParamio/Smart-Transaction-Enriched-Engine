package com.raulparamio.smarttransaction.ledger.application.port.input;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.TransactionCreateDTO;
import org.springframework.transaction.annotation.Transactional;



public interface ProcessTransactionUseCase {
    @Transactional
    void execute(TransactionCreateDTO command);


}
