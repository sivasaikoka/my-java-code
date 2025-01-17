package com.dipl.abha.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = PurposeValitor.class)
@Target( { ElementType.METHOD, ElementType.FIELD,ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PurposeType {

	String message() default "ACTION CAN NOT BE OTHER THAN LINK, KYC, KYC_AND_LINK ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}






