package com.mtarrillo.app.crud.crud_jpa.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mtarrillo.app.crud.crud_jpa.services.ProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class IsExistsDbValidation implements ConstraintValidator< IsExistsDb , String> {

    @Autowired
    private ProductService service;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       
         //esto es para que no valla directo al return
        if ( service == null  ) { //si el servicio es igual a null retorna true
            return true;
        }

        return  !service.existsBySku(value);

    }
    
}
