package com.mtarrillo.app.crud.crud_jpa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtarrillo.app.crud.crud_jpa.entities.User;
import com.mtarrillo.app.crud.crud_jpa.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> list() {

        return userService.findAll();

    }

    @PostMapping
    public ResponseEntity<?> save( @Valid @RequestBody User user ,BindingResult result) {
          
        //validation.validate(product, result);
          if (result.hasFieldErrors()) {//so ocurrio un error
            //aqui validamos
            return validation( result );
            

        }


        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));

    }





      private ResponseEntity<?> validation(BindingResult result) {


        Map<String , String > errors = new HashMap<>();
        
        
        result.getFieldErrors().forEach( err->{//por cada campo que hay un error

            //mostramos el error
            errors.put(err.getField(), "El campo "+err.getField()+" "+err.getDefaultMessage());

        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

  

    }


}
