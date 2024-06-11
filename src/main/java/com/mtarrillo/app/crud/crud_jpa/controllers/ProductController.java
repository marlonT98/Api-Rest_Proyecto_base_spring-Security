package com.mtarrillo.app.crud.crud_jpa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.mtarrillo.app.crud.crud_jpa.entities.Product;
import com.mtarrillo.app.crud.crud_jpa.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

   // @Autowired
   // private ProductValidation validation;



    @GetMapping
    public List<Product> list() {

        return productService.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {

        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) { // si esta pesente

            // respuesta de tipo 200 ok
            return ResponseEntity.ok(productOptional.orElseThrow());

        }

        // devolvemos un not found 404
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create( @Valid  @RequestBody  Product product , BindingResult result) {

    //  validation.validate(product, result);
        if (result.hasFieldErrors()) {//so ocurrio un error
            //aqui validamos
            return validation( result );
            

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));

    }

  

    @PutMapping("/{id}")
    public ResponseEntity<?> update( 
         @Valid @RequestBody Product product,
         BindingResult result,
         @PathVariable Long id) {

            //validation.validate(product, result);
            if (result.hasFieldErrors()) {//so ocurrio un error
                //aqui validamos
                return validation( result );
                
    
            }

        Optional<Product> productOptional = productService.update(id, product);
        
        if (productOptional.isPresent()) {

            return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());

        }

        return ResponseEntity.notFound().build();

    }

 

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Optional<Product> productOptional = productService.delete(id);

        if (productOptional.isPresent()) { // si esta pesente

            // respuesta de tipo 200 ok
            return ResponseEntity.ok(productOptional.orElseThrow());

        }

        // devolvemos un not found 404
        return ResponseEntity.notFound().build();
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
