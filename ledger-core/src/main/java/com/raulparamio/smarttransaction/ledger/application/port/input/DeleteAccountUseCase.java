package com.raulparamio.smarttransaction.ledger.application.port.input;

import java.util.UUID;

public interface DeleteAccountUseCase {
    void deleteById(UUID id);
}
