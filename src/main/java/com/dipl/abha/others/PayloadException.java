package com.dipl.abha.others;

import java.util.Map;

public class PayloadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Map<String,Object> errorMap;
	
	public PayloadException(Map<String, Object> errorMap) {
		this.errorMap = errorMap;
	}

	public PayloadException( Map<String,Object> errorMap,String message) {
		super(message);
		this.errorMap = errorMap;
	}

	public PayloadException(Map<String,Object> errorMap,String message, Throwable cause) {
		super(message, cause);
		this.errorMap = errorMap;
	}
	

	public Map<String, Object> getErrorMap() {
		return errorMap;
	}

}
