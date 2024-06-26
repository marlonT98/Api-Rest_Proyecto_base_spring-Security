package com.mtarrillo.app.crud.crud_jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mtarrillo.app.crud.crud_jpa.entities.User;

public interface UserRepository  extends CrudRepository< User , Long> {
    
    boolean  existsByUsername( String username );
}
