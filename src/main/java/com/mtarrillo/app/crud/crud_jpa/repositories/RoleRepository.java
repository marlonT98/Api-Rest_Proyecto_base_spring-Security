package com.mtarrillo.app.crud.crud_jpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.mtarrillo.app.crud.crud_jpa.entities.Role;

public interface RoleRepository  extends CrudRepository< Role , Long >{
    
    //buscaremos por el nombre
    Optional<Role> findByName(String name);
}
