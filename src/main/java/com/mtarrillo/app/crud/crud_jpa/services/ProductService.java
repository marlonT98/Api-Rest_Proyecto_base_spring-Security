package com.mtarrillo.app.crud.crud_jpa.services;

import java.util.List;
import java.util.Optional;

import com.mtarrillo.app.crud.crud_jpa.entities.Product;

public interface ProductService {

    // aqui tendremos los metodos sin implementar (solo las firmas de los metodos)
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    Optional<Product> update(Long id, Product product);

    Optional<Product> delete(Long id);

    boolean existsBySku(String sku);

}
