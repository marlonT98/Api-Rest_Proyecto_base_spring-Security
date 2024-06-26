package com.mtarrillo.app.crud.crud_jpa.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtarrillo.app.crud.crud_jpa.entities.User;
import com.mtarrillo.app.crud.crud_jpa.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // busamos al usuario por su nombre
        Optional<User> userOptional = repository.findByUsername(username);
        // aqui validamos
        if (userOptional.isEmpty()) {// si es vacio lanzamos nuestra exepcion
            throw new UsernameNotFoundException(String.format("username %s no existe en el sistema!", username));
        }
        User user = userOptional.orElseThrow();// optenemos el usuario de la bdd

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities);
                 

    }

}
