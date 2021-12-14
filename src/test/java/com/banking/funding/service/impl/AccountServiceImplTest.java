package com.banking.funding.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.funding.entity.AccountEntity;
import com.banking.funding.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountServiceImpl unitUnderTest;

	@Test
	void testGetAccountValidatesOwnerId() {
		assertThatThrownBy(() -> unitUnderTest.getAccount(null, 456L)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(AccountServiceImpl.OWNER_ID_IS_MANDATORY);
	}

	@Test
	void testGetAccountValidatesAccountNumber() {
		assertThatThrownBy(() -> unitUnderTest.getAccount(123L, null)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(AccountServiceImpl.ACCOUNT_NUMBER_IS_MANDATORY);
	}

	@Test
	void testGetAccountValidatesEntityExists() {
		// ARRANGE
		Mockito.when(accountRepository.findByOwnerIdAndAccountNumber(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(Optional.empty());

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.getAccount(123L, 456L)).isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("No Account found for the Owner");
	}

	@Test
	void testGetAccountReturnsSuccesfully() {
		// ARRANGE
		AccountEntity expected = new AccountEntity();
		Mockito.when(accountRepository.findByOwnerIdAndAccountNumber(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(Optional.of(expected));

		// ACT
		AccountEntity actual = unitUnderTest.getAccount(123L, 456L);

		// ASSERT
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testUpdateBalanceValidatesOwnerId() {
		assertThatThrownBy(() -> unitUnderTest.updateBalance(null, 456L, BigDecimal.ONE))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(AccountServiceImpl.OWNER_ID_IS_MANDATORY);
	}

	@Test
	void testUpdateBalanceValidatesAccountNumber() {
		assertThatThrownBy(() -> unitUnderTest.updateBalance(123L, null, BigDecimal.ONE))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(AccountServiceImpl.ACCOUNT_NUMBER_IS_MANDATORY);
	}

	@Test
	void testUpdateBalanceValidatesBalance() {
		assertThatThrownBy(() -> unitUnderTest.updateBalance(123L, 456L, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(AccountServiceImpl.BALANCE_AMOUNT_IS_MANDATORY);
	}

	@Test
	void testUpdateBalanceValidatesEntityExists() {
		// ARRANGE
		Mockito.when(accountRepository.findByOwnerIdAndAccountNumber(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(Optional.empty());

		// ACT && ASSERT
		assertThatThrownBy(() -> unitUnderTest.updateBalance(123L, 456L, BigDecimal.ONE))
				.isInstanceOf(EntityNotFoundException.class).hasMessageContaining("No Account found for the Owner");
	}

	@Test
	void testUpdateBalanceReturnsSuccesfully() {
		// ARRANGE
		AccountEntity expected = new AccountEntity();
		Mockito.when(accountRepository.findByOwnerIdAndAccountNumber(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(Optional.of(expected));
		Mockito.when(accountRepository.save(Mockito.any(AccountEntity.class)))
				.then(i -> i.getArgument(0, AccountEntity.class));

		// ACT
		AccountEntity actual = unitUnderTest.updateBalance(123L, 456L, BigDecimal.ONE);

		// ASSERT
		assertThat(actual.getBalance()).isEqualTo(expected.getBalance());
	}

}
