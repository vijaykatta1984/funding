package com.banking.funding.exceptions;

@SuppressWarnings("serial")
public class FundsNotAvailableException extends RuntimeException {

	public FundsNotAvailableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FundsNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public FundsNotAvailableException(String message) {
		super(message);
	}

	public FundsNotAvailableException(Throwable cause) {
		super(cause);
	}

}
