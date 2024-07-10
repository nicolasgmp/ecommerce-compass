package br.com.nicolas.ecommerce_compass.maps;

import br.com.nicolas.ecommerce_compass.dtos.product.ProductResponseDTO;
import br.com.nicolas.ecommerce_compass.dtos.sale.SaleRequestDTO.SaleItemRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.sale.SaleResponseDTO.SaleItemResponseDTO;
import br.com.nicolas.ecommerce_compass.models.SaleItem;

public class SaleItemMapper {

    private SaleItemMapper() {
    }

    public static SaleItem fromRequestToSaleItem(SaleItemRequestDTO dto) {
        SaleItem item = new SaleItem();
        item.setProductId(dto.productId());
        item.setQuantity(dto.quantity());
        return item;
    }

    public static SaleItemResponseDTO fromSaleItemToResponse(SaleItem dto) {
        ProductResponseDTO product = new ProductResponseDTO(
                dto.getProduct().getId(), dto.getProduct().getName(), dto.getProduct().getPrice(),
                dto.getProduct().isActive(), dto.getProduct().getStockQty(), dto.getProduct().getCreatedAt(),
                dto.getProduct().getUpdatedAt());
        return new SaleItemResponseDTO(dto.getId(), dto.getQuantity(), product);
    }

}
