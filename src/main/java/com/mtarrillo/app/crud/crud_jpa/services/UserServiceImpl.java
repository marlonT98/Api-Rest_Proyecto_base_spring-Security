package com.mtarrillo.app.crud.crud_jpa.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtarrillo.app.crud.crud_jpa.entities.User;
import com.mtarrillo.app.crud.crud_jpa.repositories.RoleRepository;
import com.mtarrillo.app.crud.crud_jpa.repositories.UserRepository;


@Service
public class UserServiceImpl  implements UserService{


    //inyectar
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)//solo leeremos - mostramos los datos para la lecturas
    @Override
    public List<User> findAll() {
    

        return (List<User>) userRepository.findAll();
    }

    @Transactional
    @Override
    public User save(User user) {

      
        return userRepository.save(user);

    }

  


}
