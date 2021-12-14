package com.banking.funding.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.banking.funding.entity.AccountEntity;
import com.banking.funding.exceptions.FundsNotAvailableException;
import com.banking.funding.rest.FundTransferRequest;
import com.banking.funding.service.AccountService;
import com.banking.funding.service.CurrencyExchangeService;
import com.banking.funding.service.FundingService;

@Service
@Transactional
public class FundingServiceImpl implements FundingService {
	static final String SAME_DEBTOR_CREDITOR_ACCOUNT_CURRENCY = "Transfer currency must be same as the Creditor Account Currency";
	static final String TRANSFER_CURRENCY_IS_MANDATORY = "Transfer currency is mandatory";
	static final String TRANSFER_AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Transfer amount must be greater than zero";
	static final String TRANSFER_AMOUNT_IS_MANDATORY = "Transfer amount is mandatory";
	static final String CREDITOR_ACCOUNT_NUMBER_IS_MANDATORY = "Creditor Account Number is mandatory";
	static final String CREDITOR_ID_IS_MANDATORY = "Creditor Id is mandatory";
	static final String DEBTOR_ACCOUNT_NUMBER_IS_MANDATORY = "Debtor Account Number is mandatory";
	static final String DEBTOR_ID_IS_MANDATORY = "Debtor Id is mandatory";
	private static final String NO_FUNDS_MESSAGE = "Funds [%s] [%s] not available in Debtor Account for the Owner [%s] and Account Number [%s]";

	private final CurrencyExchangeService exchangeService;
	private final AccountService accountService;

	public FundingServiceImpl(CurrencyExchangeService exchangeService, AccountService accountService) {
		this.exchangeService = exchangeService;
		this.accountService = accountService;
	}

	@Override
	public synchronized void depositAmount(FundTransferRequest transfer) {
		Assert.notNull(transfer.getDebtorId(), DEBTOR_ID_IS_MANDATORY);
		Assert.notNull(transfer.getDebtorAccountNumber(), DEBTOR_ACCOUNT_NUMBER_IS_MANDATORY);
		Assert.notNull(transfer.getCreditorId(), CREDITOR_ID_IS_MANDATORY);
		Assert.notNull(transfer.getCreditorAccountNumber(), CREDITOR_ACCOUNT_NUMBER_IS_MANDATORY);
		Assert.notNull(transfer.getTransferAmount(), TRANSFER_AMOUNT_IS_MANDATORY);
		Assert.isTrue(transfer.getTransferAmount().compareTo(BigDecimal.ZERO) > 0,
				TRANSFER_AMOUNT_MUST_BE_GREATER_THAN_ZERO);
		Assert.notNull(transfer.getTransferCurrency(), TRANSFER_CURRENCY_IS_MANDATORY);

		AccountEntity creditor = accountService.getAccount(transfer.getCreditorId(),
				transfer.getCreditorAccountNumber());
		Assert.isTrue(transfer.getTransferCurrency().equals(creditor.getCurrency()),
				SAME_DEBTOR_CREDITOR_ACCOUNT_CURRENCY);
		AccountEntity debtor = accountService.getAccount(transfer.getDebtorId(), transfer.getDebtorAccountNumber());

		// Convert transfer amount to Debtor Account Currency
		BigDecimal withdrawlAmount = exchangeService.convert(transfer.getTransferAmount(),
				transfer.getTransferCurrency(), debtor.getCurrency());

		// Check funds available in Debtor Account
		if (debtor.getBalance().compareTo(withdrawlAmount) < 0) {
			throw new FundsNotAvailableException(String.format(NO_FUNDS_MESSAGE, withdrawlAmount, debtor.getCurrency(),
					debtor.getOwnerId(), debtor.getAccountNumber()));
		}

		// Withdraw Funds from Debtor
		accountService.updateBalance(transfer.getDebtorId(), transfer.getDebtorAccountNumber(),
				debtor.getBalance().subtract(withdrawlAmount));

		// Deposit Funds to Creditor
		accountService.updateBalance(transfer.getCreditorId(), transfer.getCreditorAccountNumber(),
				creditor.getBalance().add(withdrawlAmount));

	}

}
