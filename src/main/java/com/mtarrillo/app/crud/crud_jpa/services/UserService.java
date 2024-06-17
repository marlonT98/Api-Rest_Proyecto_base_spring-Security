package com.mtarrillo.app.crud.crud_jpa.services;

import java.util.List;

import com.mtarrillo.app.crud.crud_jpa.entities.User;

public interface UserService  {
    
 
    List<User>  findAll( );

    User save ( User user);

    

}
