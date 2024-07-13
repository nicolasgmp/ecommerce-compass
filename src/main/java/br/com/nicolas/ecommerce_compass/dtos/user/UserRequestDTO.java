package br.com.nicolas.ecommerce_compass.dtos.user;

import java.io.Serializable;
import java.util.Set;

public record UserRequestDTO(String email, String password, Set<RoleDTO> roles) implements Serializable {
    public record RoleDTO(String role) {
    }

}
