package com.raulparamio.smarttransaction.ledger.infrastructure.output.ai;

import com.raulparamio.smarttransaction.ledger.application.port.output.AiAnalysisPort;
import com.raulparamio.smarttransaction.ledger.domain.model.Transaction;
import com.raulparamio.smarttransaction.ledger.domain.model.TransactionAnalysis;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class OllamaAiAdapter implements AiAnalysisPort {

    private final ChatClient chatClient;

    // Inyectamos el Builder para que Spring AI lo configure automáticamente
    // con la URL de Ollama definida en tu application.yml
    public OllamaAiAdapter(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    // 1. SYSTEM PROMPT: Definimos el comportamiento y las reglas
        @Override
        public TransactionAnalysis analyze(Transaction transaction) {
            return chatClient.prompt()
                    .system(sp -> sp.text("""
            Eres un analista de seguridad bancaria. 
            Tu objetivo es limpiar la descripción de la transacción y evaluar el riesgo.
            
            REGLAS PARA EL JSON:
            1. 'cleanDescription': Elimina códigos numéricos o fechas (Ej: 'MER-CAD-442' -> 'Mercadona').
            2. 'category': Elige una de [ALIMENTACION, OCIO, TRANSPORTE, SALUD, HOGAR, SUSCRIPCIONES, OTROS].
            3. 'fraudScore': Un número decimal entre 0.0 y 1.0.
            4. 'justification': Una frase breve explicando el riesgo.
            
            Responde exclusivamente en formato JSON.
            """))
                    .user(up -> up.text(String.format(
                            "Analiza esta transacción: Concepto: '%s', Importe: %s€, UsuarioID: %s",
                            transaction.getDescription(),
                            transaction.getAmount(),
                            transaction.getAccountId()))) // Asegúrate de que Transaction tenga getAccountId()
                    .call()
                    .entity(TransactionAnalysis.class); // Aquí es donde ocurre la conversión automática
        }
}