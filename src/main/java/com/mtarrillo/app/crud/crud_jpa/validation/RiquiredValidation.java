package com.mtarrillo.app.crud.crud_jpa.validation;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RiquiredValidation implements ConstraintValidator<IsRequired, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

       //return (value != null && !value.isBlank()) ;

       return StringUtils.hasText(value);//si tiene texto es true si no false
       
    }

}
