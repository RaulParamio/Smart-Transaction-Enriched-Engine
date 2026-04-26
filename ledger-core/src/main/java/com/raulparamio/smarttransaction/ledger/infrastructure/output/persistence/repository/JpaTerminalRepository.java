package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository;


import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.TerminalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTerminalRepository extends JpaRepository<TerminalEntity, String> {

}
