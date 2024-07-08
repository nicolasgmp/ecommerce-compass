package br.com.nicolas.ecommerce_compass.maps;

import br.com.nicolas.ecommerce_compass.dtos.product.ProductRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.product.ProductResponseDTO;
import br.com.nicolas.ecommerce_compass.models.Product;

public abstract class ProductMapper {

    private ProductMapper() {
    }

    public static Product fromRequestToProduct(ProductRequestDTO dto) {
        return new Product(dto.name(), dto.stockQty(), dto.price());
    }

    public static ProductResponseDTO fromProductToResponse(Product product) {
        return new ProductResponseDTO(product.getId(), product.getName(), product.getPrice(),
                product.isActive(), product.getStockQty(),
                product.getCreatedAt(), product.getUpdatedAt());
    }
}
