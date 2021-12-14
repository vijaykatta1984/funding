package com.banking.funding.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.funding.entity.AccountEntity;
import com.banking.funding.exceptions.FundsNotAvailableException;
import com.banking.funding.rest.FundTransferRequest;
import com.banking.funding.service.AccountService;
import com.banking.funding.service.CurrencyExchangeService;

@ExtendWith(MockitoExtension.class)
class FundingServiceImplTest {
	@Mock
	private CurrencyExchangeService exchangeService;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private FundingServiceImpl unitUnderTest;

	private FundTransferRequest transfer;
	private AccountEntity debtor;
	private AccountEntity creditor;

	@BeforeEach
	public void setUp() {
		transfer = new FundTransferRequest(2L, 3L, 22L, 33l, BigDecimal.TEN, Currency.getInstance("EUR"));
		debtor = new AccountEntity(1l, "F1", "L1", 2l, 3l, Currency.getInstance("USD"), BigDecimal.TEN);
		creditor = new AccountEntity(11l, "F11", "L11", 22l, 33l, Currency.getInstance("EUR"), BigDecimal.ZERO);
	}

	@Test
	void testDepositAmountValidatesDebtorId() {
		// ARRANGE
		transfer.setDebtorId(null);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.DEBTOR_ID_IS_MANDATORY);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountValidatesDebtorAccountNumber() {
		// ARRANGE
		transfer.setDebtorAccountNumber(null);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.DEBTOR_ACCOUNT_NUMBER_IS_MANDATORY);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountValidatesCreditorId() {
		// ARRANGE
		transfer.setCreditorId(null);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.CREDITOR_ID_IS_MANDATORY);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountValidatesCreditorAccountNumber() {
		// ARRANGE
		transfer.setCreditorAccountNumber(null);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.CREDITOR_ACCOUNT_NUMBER_IS_MANDATORY);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountValidatesTransferAmountMandatory() {
		// ARRANGE
		transfer.setTransferAmount(null);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.TRANSFER_AMOUNT_IS_MANDATORY);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountValidatesTransferAmountGreaterThanZero() {
		// ARRANGE
		transfer.setTransferAmount(BigDecimal.ZERO);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.TRANSFER_AMOUNT_MUST_BE_GREATER_THAN_ZERO);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountValidatesCurrency() {
		// ARRANGE
		transfer.setTransferCurrency(null);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.TRANSFER_CURRENCY_IS_MANDATORY);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountValidatesCurrencyMatcherCreditorCurrency() {
		// ARRANGE
		transfer.setTransferCurrency(Currency.getInstance("GBP"));
		when(accountService.getAccount(transfer.getCreditorId(), transfer.getCreditorAccountNumber()))
				.thenReturn(creditor);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FundingServiceImpl.SAME_DEBTOR_CREDITOR_ACCOUNT_CURRENCY);
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountChecksAvailableFunds() {
		// ARRANGE
		when(accountService.getAccount(transfer.getCreditorId(), transfer.getCreditorAccountNumber()))
				.thenReturn(creditor);
		when(accountService.getAccount(transfer.getDebtorId(), transfer.getDebtorAccountNumber())).thenReturn(debtor);
		when(exchangeService.convert(transfer.getTransferAmount(), transfer.getTransferCurrency(),
				debtor.getCurrency())).thenReturn(BigDecimal.TEN);
		debtor.setBalance(BigDecimal.ONE);

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.depositAmount(transfer)).isInstanceOf(FundsNotAvailableException.class)
				.hasMessageContaining("not available in Debtor Account");
		Mockito.verify(accountService, Mockito.never()).updateBalance(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any(BigDecimal.class));
	}

	@Test
	void testDepositAmountTransfersSuccessfully() {
		// ARRANGE
		when(accountService.getAccount(transfer.getCreditorId(), transfer.getCreditorAccountNumber()))
				.thenReturn(creditor);
		when(accountService.getAccount(transfer.getDebtorId(), transfer.getDebtorAccountNumber())).thenReturn(debtor);
		when(exchangeService.convert(transfer.getTransferAmount(), transfer.getTransferCurrency(),
				debtor.getCurrency())).thenReturn(BigDecimal.ONE);
		debtor.setBalance(BigDecimal.TEN);

		// ACT
		unitUnderTest.depositAmount(transfer);

		// ASSERT
		Mockito.verify(accountService).updateBalance(Mockito.eq(transfer.getDebtorId()),
				Mockito.eq(transfer.getDebtorAccountNumber()), Mockito.eq(new BigDecimal(9)));
	}

}
