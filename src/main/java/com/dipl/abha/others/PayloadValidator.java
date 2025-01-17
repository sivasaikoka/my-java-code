package com.dipl.abha.others;

import java.util.Map;

public interface PayloadValidator {

	public <T> Map<String, Object> validateFields(T object) throws PayloadException;

}
