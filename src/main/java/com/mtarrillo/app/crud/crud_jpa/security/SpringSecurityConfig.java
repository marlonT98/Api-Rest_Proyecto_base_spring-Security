package com.mtarrillo.app.crud.crud_jpa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean // genera un componente con una instancia de BCryptPasswordEncoder
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // AQUI ES DONDE VAMOS A CONFIGURAR TODO LO RESPECTO A SPRING SECURITY
    // este metodo devolvera un filtro donde se validara los request , autorizar,
    // dar persomisos y denegar persimos.

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

      return http.authorizeHttpRequests( (authz) -> authz
       .requestMatchers(HttpMethod.GET,  "/api/users").permitAll()//dejamos publico la ruta users
       .requestMatchers(HttpMethod.POST,  "/api/users/register").permitAll()//dejamos publico
           .anyRequest().authenticated()//los demas requiere autenticacion
      )
      .csrf( 
            config->  config.disable()//no lo necesitamos en una ApiRest
      )
      .sessionManagement(management -> 
           management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//para que la sesion http no tenga estado
           //y  todo lo que es autenticacion se maneje en el token
      )
      .build();
  

    }

}
