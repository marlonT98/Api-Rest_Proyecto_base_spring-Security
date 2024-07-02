package com.mtarrillo.app.crud.crud_jpa.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator {

     //es el contructor original de SimpleGrantedAuthority <- la clase original
     @JsonCreator
    public SimpleGrantedAuthorityJsonCreator( @JsonProperty("authority")  String role ){

    }


}
