package com.mtarrillo.app.crud.crud_jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mtarrillo.app.crud.crud_jpa.entities.Role;

public interface RoleRepository  extends CrudRepository< Role , Long >{
    
}
