package br.com.nicolas.ecommerce_compass.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.models.SaleItem;
import br.com.nicolas.ecommerce_compass.repositories.SaleItemRepository;
import br.com.nicolas.ecommerce_compass.services.interfaces.ProductService;
import br.com.nicolas.ecommerce_compass.services.interfaces.SaleItemService;

@Service
public class SaleItemServiceImpl implements SaleItemService {

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductService productService;

    public void deleteItemsFromSale(List<SaleItem> items) {
        items.stream().forEach(item -> {
            var product = productService.findById(item.getProduct().getId());
            productService.returnItemsToProductStock(product, item.getQuantity());
            saleItemRepository.delete(item);
        });
    }
}
