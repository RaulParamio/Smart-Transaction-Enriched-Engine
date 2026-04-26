package com.raulparamio.smarttransaction.ledger.application.port.input;

import com.raulparamio.smarttransaction.ledger.application.port.input.dto.*;
import org.springframework.transaction.annotation.Transactional;

public interface ProcessTransactionUseCase {

    @Transactional
    TransactionResponseDTO processCardPayment(CardPaymentDTO command);

    @Transactional
    TransactionResponseDTO processBizum(BizumDTO command);

    @Transactional
    TransactionResponseDTO processTransfer(SepaTransferDTO command);

}