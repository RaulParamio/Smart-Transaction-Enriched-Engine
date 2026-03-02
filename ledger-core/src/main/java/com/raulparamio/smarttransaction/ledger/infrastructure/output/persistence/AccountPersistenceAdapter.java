package com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence;

import com.raulparamio.smarttransaction.ledger.domain.model.Account;
import com.raulparamio.smarttransaction.ledger.domain.port.output.AccountRepositoryPort;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.entity.AccountEntity;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.mapper.AccountMapper;
import com.raulparamio.smarttransaction.ledger.infrastructure.output.persistence.repository.JpaAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountRepositoryPort {

    private final JpaAccountRepository jpaAccountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account save(Account account) {
        // 1. Convertir Dominio -> Entidad
        AccountEntity entity = accountMapper.toEntity(account);
        // 2. Guardar en BBDD
        AccountEntity savedEntity = jpaAccountRepository.save(entity);
        // 3. Convertir Entidad -> Dominio y devolver
        return accountMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findByIban(String iban) {
        return jpaAccountRepository.findByIban(iban)
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaAccountRepository.findById(id)
                .map(accountMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaAccountRepository.deleteById(id);
    }
}
