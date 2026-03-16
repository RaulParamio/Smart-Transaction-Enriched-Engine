package com.raulparamio.smarttransaction.ledger.infrastructure.output.ai;


import com.raulparamio.smarttransaction.ledger.application.port.output.AiAnalysisPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FakeAiAdapter implements AiAnalysisPort {

    @Override
    public TransactionAnalysis analyze(Transaction transaction) {
        // Devolvemos un análisis fijo al instante sin esperar a Llama 3
        return TransactionAnalysis.builder()
                .transactionId(transaction.getTransactionId())
                .cleanDescription("Empresa Simulada S.L.")
                .category("PRUEBA_SISTEMA")
                .fraudScore(new BigDecimal("0.01"))
                .justification("Análisis generado automáticamente por el FakeAdapter para validar la persistencia.")
                .build();
    }
}
