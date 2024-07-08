package br.com.nicolas.ecommerce_compass.dtos.sale;

import java.io.Serializable;
import java.util.List;

public record SaleRequestDTO(List<SaleItemRequestDTO> items) implements Serializable {
    public record SaleItemRequestDTO(Long productId, Integer quantity) implements Serializable {
    }
}
