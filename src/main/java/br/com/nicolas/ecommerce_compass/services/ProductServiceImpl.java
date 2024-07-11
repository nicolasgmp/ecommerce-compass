package br.com.nicolas.ecommerce_compass.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.exceptions.DuplicateEntryException;
import br.com.nicolas.ecommerce_compass.exceptions.EntityValidationException;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.models.Product;
import br.com.nicolas.ecommerce_compass.repositories.ProductRepository;
import br.com.nicolas.ecommerce_compass.services.interfaces.ProductService;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "products", key = "#id")
    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado no banco de dados com o id: " + id));
    }

    @Cacheable(value = "products", key = "'all'")
    public List<Product> findAll() {
        var products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Não foram encontrados produtos na base de dados");
        }
        return products;
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Product create(Product product) {
        product.setActive(true);
        this.verifyByName(product.getName());
        if (product.getName() == null || product.getName().isBlank()) {
            throw new EntityValidationException("O nome não pode estar vazio");
        }
        this.verifyStockQtyNegativeOrZero(product);
        return productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Product update(Product product, UUID id) {
        var old = this.findById(id);
        this.verifyStockQtyNegativeOrZero(product);
        if (product.getStockQty() >= 0) {
            this.verifyStockQtySmallerThanActual(product, old);
        }
        if (!old.isActive()) {
            throw new EntityValidationException("Um produto inativo não pode ser atualizado");
        }
        old.setPrice(product.getPrice());
        old.setStockQty(product.getStockQty());
        old.setUpdatedAt();
        return productRepository.save(old);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void delete(UUID id) {
        var toDelete = this.findById(id);
        if (toDelete.isSelled()) {
            throw new EntityValidationException("Um produto que já foi vendido não pode ser excluído");
        }
        productRepository.delete(toDelete);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void changeProductState(UUID id) {
        var product = this.findById(id);
        product.setActive(!product.isActive());
        productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void subtractStock(int quantity, Product product) {
        if (product.getStockQty() < quantity) {
            throw new EntityValidationException("Estoque insuficiente para realizar a venda");
        }
        product.setStockQty(product.getStockQty() - quantity);
        productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void returnItemsToProductStock(Product product, Integer qty) {
        product.setStockQty(product.getStockQty() + qty);
        productRepository.save(product);
    }

    private void verifyByName(String name) {
        var product = productRepository.findByName(name);
        if (product.isPresent()) {
            throw new DuplicateEntryException("Produto com o nome " + name + " já existe");
        }
    }

    private void verifyStockQtyNegativeOrZero(Product product) {
        if (product.getStockQty() <= 0) {
            throw new EntityValidationException("O estoque não pode ser negativo ou igual a 0");
        }
    }

    private void verifyStockQtySmallerThanActual(Product newP, Product oldP) {
        if (newP.getStockQty() < oldP.getStockQty()) {
            throw new EntityValidationException("O estoque não pode ser menor que o atual");
        }
    }
}
