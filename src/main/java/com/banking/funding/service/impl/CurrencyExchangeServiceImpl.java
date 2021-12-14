package com.banking.funding.service.impl;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.banking.funding.service.CurrencyExchangeService;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
	static final String AMOUNT_IS_MANDATORY = "Amount is mandatory";
	static final String AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Amount must be greater than zero";
	static final String SOURCE_CURRENCY_IS_MANDATORY = "Source currency is mandatory";
	static final String DESTINATION_CURRENCY_IS_MANDATORY = "Destination currency is mandatory";

	@Override
	public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
		Assert.notNull(amount, AMOUNT_IS_MANDATORY);
		Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, AMOUNT_MUST_BE_GREATER_THAN_ZERO);
		Assert.notNull(from, SOURCE_CURRENCY_IS_MANDATORY);
		Assert.notNull(to, DESTINATION_CURRENCY_IS_MANDATORY);

		if (from.equals(to)) {
			return amount;
		} else {
			return amount.multiply(BigDecimal.valueOf(1 + new Random().nextDouble()));
		}
	}

}
