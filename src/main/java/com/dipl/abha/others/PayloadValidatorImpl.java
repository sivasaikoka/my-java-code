package com.dipl.abha.others;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Component;

@Component
public class PayloadValidatorImpl implements PayloadValidator {

	private ValidatorFactory valdiatorFactory = null;

	public PayloadValidatorImpl() {
		valdiatorFactory = Validation.buildDefaultValidatorFactory();
	}

	@Override
	public <T> Map<String, Object> validateFields(T object) throws PayloadException {
		Map<String, Object> errorMap = new HashMap<>();
		try {
			Validator validator = valdiatorFactory.getValidator();
			Set<ConstraintViolation<T>> failedValidations = validator.validate(object);

			if (!failedValidations.isEmpty()) {
				List<String> allErrors = failedValidations.stream().map(failure -> failure.getMessage())
						.collect(Collectors.toList());
				errorMap.put("message", allErrors);
			}
			if (!errorMap.isEmpty()) {
				throw new PayloadException(errorMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (errorMap.isEmpty()) {
				errorMap.put("message", "Invalid Payload,Please try again..!");
			}
			throw new PayloadException(errorMap);
		}
		return errorMap;
	}

}
