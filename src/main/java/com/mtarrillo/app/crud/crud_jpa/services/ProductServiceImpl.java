package com.mtarrillo.app.crud.crud_jpa.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtarrillo.app.crud.crud_jpa.entities.Product;
import com.mtarrillo.app.crud.crud_jpa.repositories.ProductRepository;

/**
 * ProductServiceImpl
 */

@Service // da un stereotipo para indicar que es un componente
// aqui es donde implementamos los metodos creados en la interface de
// productservice
public class ProductServiceImpl implements ProductService {

    // inyectamos
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {

        return (List<Product>) productRepository.findAll();

    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {

        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public Product save(Product product) {

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, Product product) {

        // buscamos en la base de datos por su id
        Optional<Product> productOptional = productRepository.findById(id);

        // si esta presente es actualizado
        if (productOptional.isPresent()) {

            Product productDb = productOptional.orElseThrow();
            // pasamos los nuevos datos del producto que nos pasao por parametros
            productDb.setSku(product.getSku());
            productDb.setName(product.getName());
            productDb.setPrice(product.getPrice());
            productDb.setDescription(product.getDescription());

            // guardamos el producto seteado con los datos del parametro pasado
            return Optional.of(productRepository.save(productDb));

        }
       

        return productOptional;

    }

    @Transactional
    @Override
    public Optional<Product> delete(Long id) {

        // buscamos en la base de datos por su id
        Optional<Product> productOptional = productRepository.findById(id);

        // si esta presente es borrado
        productOptional.ifPresent(productDb -> {

            productRepository.delete(productDb);

        });

        return productOptional;

    }

    @Override
    @Transactional(readOnly = true)//solo es lectura
    public boolean existsBySku(String sku) {
       
       return  productRepository.existsBySku(sku);

    }

}