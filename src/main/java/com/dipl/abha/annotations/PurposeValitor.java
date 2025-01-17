package com.dipl.abha.annotations;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PurposeValitor implements ConstraintValidator<PurposeType, String>{
	
	private static final String[] ACTION_TYPE = {"LINK", "KYC", "KYC_AND_LINK" };
	
    @Override
	public void initialize(PurposeType constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && Arrays.asList(ACTION_TYPE).contains(value);
	}

}

