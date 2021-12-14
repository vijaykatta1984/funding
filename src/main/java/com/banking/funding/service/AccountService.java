package com.banking.funding.service;

import java.math.BigDecimal;

import com.banking.funding.entity.AccountEntity;

public interface AccountService {

	AccountEntity getAccount(Long ownerId, Long accountNumber);

	AccountEntity updateBalance(Long ownerId, Long accountNumber, BigDecimal amount);

}
