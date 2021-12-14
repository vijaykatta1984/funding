package com.banking.funding.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.Test;

class CurrencyExchangeServiceImplTest {
	private static final Currency EUR = Currency.getInstance("EUR");
	private static final Currency USD = Currency.getInstance("USD");
	private CurrencyExchangeServiceImpl unitUnderTest = new CurrencyExchangeServiceImpl();

	@Test
	void testConvertValidatesAmount() {
		assertThatThrownBy(() -> unitUnderTest.convert(null, USD, EUR)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(CurrencyExchangeServiceImpl.AMOUNT_IS_MANDATORY);
	}

	@Test
	void testConvertValidatesAmountGreaterThanZero() {
		assertThatThrownBy(() -> unitUnderTest.convert(BigDecimal.ZERO, USD, EUR))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(CurrencyExchangeServiceImpl.AMOUNT_MUST_BE_GREATER_THAN_ZERO);
	}

	@Test
	void testConvertValidatesFromCurrency() {
		assertThatThrownBy(() -> unitUnderTest.convert(BigDecimal.ONE, null, EUR))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(CurrencyExchangeServiceImpl.SOURCE_CURRENCY_IS_MANDATORY);
	}

	@Test
	void testConvertValidatesToCurrency() {
		assertThatThrownBy(() -> unitUnderTest.convert(BigDecimal.ONE, USD, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(CurrencyExchangeServiceImpl.DESTINATION_CURRENCY_IS_MANDATORY);
	}

	@Test
	void testConvertReturnsActualAmountIfBothCurrenciesAreSame() {
		assertThat(unitUnderTest.convert(BigDecimal.ONE, USD, USD)).isEqualTo(BigDecimal.ONE);
	}

	@Test
	void testConvertReturnsSuccessfully() {
		assertThat(unitUnderTest.convert(BigDecimal.ONE, USD, EUR)).isGreaterThan(BigDecimal.ONE);
	}

}
