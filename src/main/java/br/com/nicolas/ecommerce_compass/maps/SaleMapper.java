package br.com.nicolas.ecommerce_compass.maps;

import java.util.List;

import br.com.nicolas.ecommerce_compass.dtos.sale.SaleRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.sale.SaleResponseDTO;
import br.com.nicolas.ecommerce_compass.dtos.sale.SaleResponseDTO.SaleItemResponseDTO;
import br.com.nicolas.ecommerce_compass.models.Sale;
import br.com.nicolas.ecommerce_compass.models.SaleItem;

public class SaleMapper {

    public static Sale fromRequestToSale(SaleRequestDTO request) {
        Sale sale = new Sale();
        List<SaleItem> items = request.items().stream().map(SaleItemMapper::fromRequestToSaleItem).toList();
        sale.setItems(items);
        return sale;
    }

    public static SaleResponseDTO fromSaleToResponse(Sale sale) {
        List<SaleItemResponseDTO> items = sale.getItems().stream()
                .map(SaleItemMapper::fromSaleItemToResponse).toList();

        return new SaleResponseDTO(
                sale.getId(), sale.getCreatedAt(), sale.getUpdatedAt(),
                sale.getTotal(), items);
    }
}
