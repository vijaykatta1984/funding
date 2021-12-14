package com.banking.funding.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.banking.funding.entity.AccountEntity;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
	Optional<AccountEntity> findByOwnerIdAndAccountNumber(Long ownerId, Long accountNumber);
}