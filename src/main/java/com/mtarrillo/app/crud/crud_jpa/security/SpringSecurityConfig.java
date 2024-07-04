package com.mtarrillo.app.crud.crud_jpa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.mtarrillo.app.crud.crud_jpa.security.filter.JwtAuthenticationFilter;
import com.mtarrillo.app.crud.crud_jpa.security.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled=true)
public class SpringSecurityConfig {



    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    //nos permiete generar y obtener el authentication manager de nuestro spring secirity
    AuthenticationManager authenticationManager() throws Exception{

        return authenticationConfiguration.getAuthenticationManager();
    }



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
       .requestMatchers(HttpMethod.GET,  "/api/users").permitAll()//listar los usuarios
       .requestMatchers(HttpMethod.POST,  "/api/users/register").permitAll()//se registra un usuario
    //    .requestMatchers(HttpMethod.POST,  "/api/users").hasRole("ADMIN")//solo crea usuarios  admin
    //    .requestMatchers(HttpMethod.GET,  "/api/products","/api/products/{id}").hasAnyRole("ADMIN","USER")
    //    .requestMatchers(HttpMethod.POST,  "/api/products").hasRole("ADMIN")//solo crear productos el admin
    //    .requestMatchers(HttpMethod.PUT,  "/api/products/{id}").hasRole("ADMIN")//solo modifica el admin
    //    .requestMatchers(HttpMethod.DELETE,  "/api/products/{id}").hasRole("ADMIN")//solo elimina el admin
           .anyRequest().authenticated()//los demas requiere autenticacion
      )
      .addFilter( new  JwtAuthenticationFilter( authenticationManager()  ))//pasamos por argumento( nuestro metodo)
      .addFilter( new  JwtValidationFilter( authenticationManager() ))//pasamos por argumento( nuestro metodo)
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
