package com.dipl.abha.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
public class TenantNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 5825531453678916319L;

	public TenantNotFoundException(String message) {
		super(message);
	}

	public TenantNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
