package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository;

import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface JpaAccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByIban(String iban);
}