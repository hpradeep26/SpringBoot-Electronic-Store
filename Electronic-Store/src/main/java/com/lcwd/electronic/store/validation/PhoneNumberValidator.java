package com.lcwd.electronic.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
            return false;
        }
       return value.matches("\\d{8,11}");
	}



}
