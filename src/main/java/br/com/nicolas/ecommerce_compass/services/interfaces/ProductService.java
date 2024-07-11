package br.com.nicolas.ecommerce_compass.services.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.nicolas.ecommerce_compass.models.Product;

public interface ProductService {
    Product findById(UUID id);

    List<Product> findAll();

    Product create(Product product);

    Product update(Product product, UUID id);

    void delete(UUID id);

    void changeProductState(UUID id);

    void subtractStock(int quantity, Product product);

    void returnItemsToProductStock(Product product, Integer qty);
}
