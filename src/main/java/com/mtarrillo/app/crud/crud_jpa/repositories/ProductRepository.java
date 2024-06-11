package com.mtarrillo.app.crud.crud_jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mtarrillo.app.crud.crud_jpa.entities.Product;

public interface ProductRepository  extends CrudRepository< Product , Long >{
    
    //si existe el sku true si no false
    boolean existsBySku(String sku);
}
