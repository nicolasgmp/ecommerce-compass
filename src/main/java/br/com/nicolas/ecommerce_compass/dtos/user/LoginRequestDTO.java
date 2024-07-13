package br.com.nicolas.ecommerce_compass.dtos.user;

import java.io.Serializable;

public record LoginRequestDTO(String email, String password) implements Serializable {

}
