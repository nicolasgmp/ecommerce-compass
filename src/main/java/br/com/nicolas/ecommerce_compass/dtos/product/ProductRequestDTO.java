package br.com.nicolas.ecommerce_compass.dtos.product;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record ProductRequestDTO(
                String name, @NotNull Integer stockQty, @NotNull BigDecimal price) implements Serializable {

}
