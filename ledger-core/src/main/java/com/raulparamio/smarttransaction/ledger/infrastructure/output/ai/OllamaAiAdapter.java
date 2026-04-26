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


    @Override
    public TransactionAnalysis analyze(Transaction transaction) {
        return chatClient.prompt()
                .system(sp -> sp.text("""
    Eres un experto en fraude bancario. Tu misión es normalizar descripciones y evaluar riesgos.

    --- IDENTIFICACIÓN DE MÉTODO (Para cleanDescription) ---
    Analiza el campo 'Datos' y aplica este formato: "[Método] en [Comercio]"
    
    1. Si ves 'Mode: 05' -> "Pago con Chip en [Comercio]"
    2. Si ves 'Mode: 07' -> "Pago Contactless en [Comercio]"
    3. Si ves 'Mode: 81' -> "Pago Online en [Comercio]"
    
    Si es Bizum -> "Bizum de [Concepto]"
    Si es Transferencia -> "Transferencia: [Concepto]"

    --- REGLAS DE NEGOCIO ---
    - 'category': [ALIMENTACION, OCIO, TRANSPORTE, SALUD, HOGAR, SUSCRIPCIONES, OTROS].
    - 'fraudScore': Decimal de 0.0 a 1.0 (0.1 = 10%).
    - 'justification': Frase breve con el % de riesgo. Ej: "Riesgo del 5%. Método físico seguro."

    --- PRIORIDADES DE CATEGORÍA ---
    - Mercadona, Lidl, Carrefour, UberEats -> ALIMENTACION.
    - Netflix, Spotify, Amazon Prime, DAZN -> SUSCRIPCIONES.
    - Renfe, Uber, Cabify, Gasolinera -> TRANSPORTE.
    """))
                .user(up -> up.text(String.format(
                        "DATOS DE ENTRADA: Concepto/Descripción: '%s' | Importe: %s€ | ID Usuario: %s",
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getSourceAccountId())))
                .call()
                .entity(TransactionAnalysis.class);
    }
}