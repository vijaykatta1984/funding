package com.banking.funding.rest;

import java.math.BigDecimal;
import java.util.Currency;

public class FundTransferRequest {
	private Long debtorId;
	private Long debtorAccountNumber;
	private Long creditorId;
	private Long creditorAccountNumber;
	private BigDecimal transferAmount;
	private Currency transferCurrency;

	public FundTransferRequest() {
	}

	public FundTransferRequest(Long debtorId, Long debtorAccountNumber, Long creditorId, Long creditorAccountNumber,
			BigDecimal transferAmount, Currency transferCurrency) {
		this.debtorId = debtorId;
		this.debtorAccountNumber = debtorAccountNumber;
		this.creditorId = creditorId;
		this.creditorAccountNumber = creditorAccountNumber;
		this.transferAmount = transferAmount;
		this.transferCurrency = transferCurrency;
	}

	public Long getDebtorId() {
		return debtorId;
	}

	public void setDebtorId(Long debtorId) {
		this.debtorId = debtorId;
	}

	public Long getDebtorAccountNumber() {
		return debtorAccountNumber;
	}

	public void setDebtorAccountNumber(Long debtorAccountNumber) {
		this.debtorAccountNumber = debtorAccountNumber;
	}

	public Long getCreditorId() {
		return creditorId;
	}

	public void setCreditorId(Long creditorId) {
		this.creditorId = creditorId;
	}

	public Long getCreditorAccountNumber() {
		return creditorAccountNumber;
	}

	public void setCreditorAccountNumber(Long creditorAccountNumber) {
		this.creditorAccountNumber = creditorAccountNumber;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Currency getTransferCurrency() {
		return transferCurrency;
	}

	public void setTransferCurrency(Currency transferCurrency) {
		this.transferCurrency = transferCurrency;
	}

}
