package com.banking.funding.entity;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String firstName;
	private String lastName;
	private Long ownerId;
	private Long accountNumber;
	private Currency currency;
	private BigDecimal balance;

	public AccountEntity() {
	}

	public AccountEntity(Long id, String firstName, String lastName, Long ownerId, Long accountNumber,
			Currency currency, BigDecimal balance) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.ownerId = ownerId;
		this.accountNumber = accountNumber;
		this.currency = currency;
		this.balance = balance;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Currency getCurrency() {
		return currency;
	}

	public String getFirstName() {
		return firstName;
	}

	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
