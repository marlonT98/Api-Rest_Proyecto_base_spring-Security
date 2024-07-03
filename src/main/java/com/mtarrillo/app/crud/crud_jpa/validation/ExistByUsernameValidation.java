package com.mtarrillo.app.crud.crud_jpa.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mtarrillo.app.crud.crud_jpa.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistByUsernameValidation implements ConstraintValidator<ExistByUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {

        //esto es para que no valla directo al return
        if ( userService == null  ) {//si el servicio es valido retorna true
            return true;
        }

        return !userService.existsByUsername(username);

    }

}
