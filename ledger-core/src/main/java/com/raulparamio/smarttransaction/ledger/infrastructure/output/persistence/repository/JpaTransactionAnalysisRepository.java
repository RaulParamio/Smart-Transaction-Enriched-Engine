package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository;


import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TransactionAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaTransactionAnalysisRepository extends JpaRepository<TransactionAnalysisEntity, UUID> {
}
