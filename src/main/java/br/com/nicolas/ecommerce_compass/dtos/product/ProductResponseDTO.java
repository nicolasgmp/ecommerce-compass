package br.com.nicolas.ecommerce_compass.dtos.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponseDTO(
        Long id, String name, BigDecimal price,
        Boolean active, Integer stockQty, Instant createdAt,
        Instant updatedAt) implements Serializable {
}
