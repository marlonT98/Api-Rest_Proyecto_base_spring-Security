package com.mtarrillo.app.crud.crud_jpa.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtarrillo.app.crud.crud_jpa.entities.Role;
import com.mtarrillo.app.crud.crud_jpa.entities.User;
import com.mtarrillo.app.crud.crud_jpa.repositories.RoleRepository;
import com.mtarrillo.app.crud.crud_jpa.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    // inyectar
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true) // solo leeremos - mostramos los datos para la lecturas
    @Override
    public List<User> findAll() {

        return (List<User>) userRepository.findAll();
    }

    @Transactional
    @Override
    public User save(User user) {

        // llenando los roles al usuario si solo es user o admin
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");// es es si o si
        List<Role> roles = new ArrayList<>();

        optionalRoleUser.ifPresent(role -> roles.add(role));// otra forma (roles:add)

        if (user.isAdmin()) {// este depende de si es admin

            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");

            optionalRoleAdmin.ifPresent(roles::add);

        }

        // establecemos los roles
        user.setRoles(roles);
        // solo faltaria el password encriptado
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        //guardamos con los roles pasados y codificamos el password
        return userRepository.save(user);

    }

}
