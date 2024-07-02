package com.mtarrillo.app.crud.crud_jpa.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtarrillo.app.crud.crud_jpa.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//importando nuestra clase que tiene las constantes
import static  com.mtarrillo.app.crud.crud_jpa.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //pasamos via constructor
    private AuthenticationManager authenticationManager;

    //constructor
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
    }
    

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username =user.getUsername();
            password =user.getPassword();
        } catch (StreamReadException e) {

            e.printStackTrace();
        } catch (DatabindException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return authenticationManager.authenticate(authenticationToken);

    }


    //metodo cuando todo sale bien
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
     

                //estamos haciendo referencia al User de spring no del nuestro (por eso estamos espicificando desde su paquete)
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();//devuelve un objeto usuario 
                String username =user.getUsername(); //aqui obtenemos el usernmae

                //roles
                Collection<? extends GrantedAuthority> roles  =   authResult.getAuthorities();//devuelve un tipo
                //los roles tenemos que pasar a los claims(los claims son  son datos)
                Claims claims = Jwts.claims()
                      .add("authorities", new ObjectMapper().writeValueAsString(roles) )//pasamos los roles como un json
                      .add("username",username)
                      .build();

            
                //creamos el el token
                String token = Jwts.builder()
                .subject(username)//pasamos el usuario
                .claims(claims)//pasamos los datos
                .expiration(new Date(System.currentTimeMillis() + 3600000))//fecha actul + una hora
                .issuedAt(new Date())//fecha actual
                .signWith(SECRET_KEY)
                .compact();


                response.addHeader(HEADER_AUTHORIZATION,PREFINX_TOKEN+ token);

                Map<String ,String> body = new HashMap<>();
                body.put("token", token);
                body.put("username", username);
                body.put("message", String.format("Hola %s has iniciado sesion con exito", username));


                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setContentType(CONTENT_TYPE);
                response.setStatus(200);



    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        

                Map<String ,String> body = new HashMap<>();
                body.put("message","Error en la autenticacion username o password!");
                body.put("error", failed.getMessage());

                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setContentType(CONTENT_TYPE);
                response.setStatus(401);//no esta autorizado


    }



    


}
