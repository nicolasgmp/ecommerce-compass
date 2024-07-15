package br.com.nicolas.ecommerce_compass.dtos.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.nicolas.ecommerce_compass.dtos.product.ProductResponseDTO;

public record SaleResponseDTO(
        UUID id, Instant createdAt,
        Instant updatedAt, BigDecimal total,
        List<SaleItemResponseDTO> items, String userEmail) implements Serializable {

    public record SaleItemResponseDTO(UUID id, Integer quantity, ProductResponseDTO product) {
    }

}
