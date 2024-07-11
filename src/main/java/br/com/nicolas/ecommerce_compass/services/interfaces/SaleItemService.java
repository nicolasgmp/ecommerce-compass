package br.com.nicolas.ecommerce_compass.services.interfaces;

import java.util.List;

import br.com.nicolas.ecommerce_compass.models.SaleItem;

public interface SaleItemService {

    void deleteItemsFromSale(List<SaleItem> items);
}
