package br.com.nicolas.ecommerce_compass.dtos.sale;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import br.com.nicolas.ecommerce_compass.dtos.product.ProductResponseDTO;

public record SaleResponseDTO(
        Long id, Instant createdAt,
        Instant updatedAt, BigDecimal total,
        List<SaleItemResponseDTO> items) {

    public record SaleItemResponseDTO(Long id, Integer quantity, ProductResponseDTO product) {
    }

}
