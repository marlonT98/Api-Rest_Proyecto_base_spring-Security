package com.mtarrillo.app.crud.crud_jpa.security.filter;

import static com.mtarrillo.app.crud.crud_jpa.security.TokenJwtConfig.CONTENT_TYPE;
import static com.mtarrillo.app.crud.crud_jpa.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.mtarrillo.app.crud.crud_jpa.security.TokenJwtConfig.PREFINX_TOKEN;
import static com.mtarrillo.app.crud.crud_jpa.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtarrillo.app.crud.crud_jpa.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    // BasicAuthenticationFilter tiene un contructor que recibe el
    // authenticationManager

    // por eso estamos pasando mediante el super
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // obtenemos la cabecera (los tokens se envian en la cabecera)
        // obtenemos el token que enviamos desde el postamn o el front
        String header = request.getHeader(HEADER_AUTHORIZATION);

        // verificamos si la cabecera es null o no existe el prefijo Bearer
        if (header == null || !header.startsWith(PREFINX_TOKEN)) {

            chain.doFilter(request, response);//para que siga el filtro
            return;
        }

        // quitamos el Bearer ( solo dejamos al token)
        String token = header.replace(PREFINX_TOKEN, "");

        // ***comenzamos a validar***
        try {

            //validamos el token
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)//verificamos con nuestra llave que esta creadaen la clase TokenJwtConfig
                    .build()
                    .parseSignedClaims(token)//parsesamos el token
                    .getPayload();//obtenemos el playload 
            String username = claims.getSubject();//obtenemos el username
            Object authoritiesClaims = claims.get("authorities");//obtenemos los roles

            //onvertimos los roles que vienen en estructura string como json lo convertimos a un objeto GrantedAuthority
            //osea un tipo collection
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                    .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)//los roles vienen con los nombres de campo y por eso lo pasmos al contructor como rol 
                     .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));


                     //iniciamos sesion 
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);

            // estamos auntenticando
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            //cadena de los filtros
            chain.doFilter(request, response);

        } catch (JwtException e) {

            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token jwt es invalido");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());// no esta autorizado(401)
            response.setContentType(CONTENT_TYPE);

        }

    }

}
