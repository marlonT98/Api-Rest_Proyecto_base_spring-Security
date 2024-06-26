package com.mtarrillo.app.crud.crud_jpa.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mtarrillo.app.crud.crud_jpa.validation.ExistByUsername;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExistByUsername
    @Column(unique = true)
    @NotBlank//validamos que no sea vacio
    @Size( min = 4 , max = 12 )//tamaño de caracteres autorizados
    private String username;

    @NotBlank//validamos que no sea vacio
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//ya no lo muestra en el json
    private String password;

    @JsonIgnoreProperties({"users","handler","hibernateLazyInitializer"})//ignora la propiedad actual 
    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="role_id"),
        uniqueConstraints = { @UniqueConstraint( columnNames = {"user_id","role_id"}) }
    )
    private List< Role > roles;

    //inicializamos la lista de roles por medio de un constructor
    public User() {
       roles = new ArrayList<>();
    }

    @Transient//es un campo que no es de table
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//ya no lo muestra en el json
    private boolean admin;//este no es un campo de la bdd (solamente es una bandera)


    //inicializando enabled en true
    @PrePersist 
    public void PrePersist(){
        enabled=true;
    }

    //este campo es el que me interesa para saber si esta habilitado o no
    private boolean enabled;//para desabilitar un rol (ejemplo para adesabilitar un usuario)



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
    
    




}
