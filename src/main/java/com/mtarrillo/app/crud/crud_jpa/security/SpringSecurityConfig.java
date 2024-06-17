package com.mtarrillo.app.crud.crud_jpa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig {
    
    @Bean //genera un componente con una instancia de BCryptPasswordEncoder
    PasswordEncoder passwordEncoder( ){
        return new BCryptPasswordEncoder();
    }

}
