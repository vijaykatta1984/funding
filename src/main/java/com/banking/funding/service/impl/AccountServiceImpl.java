package com.banking.funding.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.banking.funding.entity.AccountEntity;
import com.banking.funding.repository.AccountRepository;
import com.banking.funding.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	static final String BALANCE_AMOUNT_IS_MANDATORY = "Balance Amount is mandatory";
	static final String ACCOUNT_NUMBER_IS_MANDATORY = "Account Number is mandatory";
	static final String OWNER_ID_IS_MANDATORY = "Owner Id is mandatory";
	static final String NOT_FOUND_MESSAGE = "No Account found for the Owner [%s] and Account Number [%s]";

	private final AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public AccountEntity getAccount(Long ownerId, Long accountNumber) {
		Assert.notNull(ownerId, OWNER_ID_IS_MANDATORY);
		Assert.notNull(accountNumber, ACCOUNT_NUMBER_IS_MANDATORY);

		Optional<AccountEntity> account = accountRepository.findByOwnerIdAndAccountNumber(ownerId, accountNumber);
		return account.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, ownerId, accountNumber)));
	}

	@Override
	public AccountEntity updateBalance(Long ownerId, Long accountNumber, BigDecimal balance) {
		Assert.notNull(balance, BALANCE_AMOUNT_IS_MANDATORY);

		AccountEntity account = getAccount(ownerId, accountNumber);
		account.setBalance(balance);
		return accountRepository.save(account);
	}

}
