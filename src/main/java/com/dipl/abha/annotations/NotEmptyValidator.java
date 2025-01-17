package com.dipl.abha.annotations;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {
		
	
	private String jsonPropertyName = "";
    @Override
	public void initialize(NotEmpty constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		jsonPropertyName = constraintAnnotation.columnName();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if( value != null) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(jsonPropertyName+" should not be empty").addConstraintViolation();
		return false;
	}
}
