package br.com.nicolas.ecommerce_compass.dtos.user;

import java.io.Serializable;

import br.com.nicolas.ecommerce_compass.models.UserRole;

public record RegisterDTO(String email, String password, UserRole role) implements Serializable {
}
