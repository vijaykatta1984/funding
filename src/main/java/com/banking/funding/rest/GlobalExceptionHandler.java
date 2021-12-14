package com.banking.funding.rest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.banking.funding.exceptions.FundsNotAvailableException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
		return handle(ex, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	protected ResponseEntity<Object> handleValidation(RuntimeException ex, WebRequest request) {
		return handle(ex, request, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = FundsNotAvailableException.class)
	protected ResponseEntity<Object> handleFundsNotAvailable(FundsNotAvailableException ex, WebRequest request) {
		return handle(ex, request, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Exception.class)
	protected ResponseEntity<Object> handleDefaultException(Exception ex, WebRequest request) {
		return handle(ex, request, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Object> handle(Exception ex, WebRequest request, HttpStatus status) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());

		return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
	}
}
