package com.dipl.abha.annotations;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class RequestTypeValidator implements ConstraintValidator<RequesterType, String>{
	
	private static final String[] REQUESTER_TYPE = {"HIP", "HIU" };
	
    @Override
	public void initialize(RequesterType constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && Arrays.asList(REQUESTER_TYPE).contains(value);
	}

}
