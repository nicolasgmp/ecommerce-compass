package br.com.nicolas.ecommerce_compass.dtos.sale;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record SaleRequestDTO(List<SaleItemRequestDTO> items) implements Serializable {
    public record SaleItemRequestDTO(UUID productId, Integer quantity) implements Serializable {
    }
}
