package br.com.nicolas.ecommerce_compass.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.exceptions.DuplicateEntryException;
import br.com.nicolas.ecommerce_compass.exceptions.EntityValidationException;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.models.Product;
import br.com.nicolas.ecommerce_compass.repositories.ProductRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "products")
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado no banco de dados com o id: " + id));
    }

    public void findByName(String name) {
        var product = productRepository.findByName(name);
        if (product.isPresent()) {
            throw new DuplicateEntryException("Produto com o nome " + name + " já existe");
        }
    }

    @Cacheable(value = "products")
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
        findByName(product.getName());
        if (product.getStockQty() <= 0) {
            throw new EntityValidationException("O estoque não pode ser negativo ou igual a 0");
        }
        return productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Product update(Product product, Long id) {
        findByName(product.getName());
        var old = findById(id);
        if (product.getStockQty() <= 0) {
            throw new EntityValidationException("O estoque não pode ser negativo ou igual a 0");
        }
        if (product.getStockQty() >= 0) {
            if (product.getStockQty() < old.getStockQty()) {
                throw new EntityValidationException("O estoque não pode ser menor do que o atual");
            }
        }
        if (!old.isActive()) {
            throw new EntityValidationException("Um produto inativo não pode ser atualizado");
        }
        old.setName(product.getName());
        old.setPrice(product.getPrice());
        old.setStockQty(product.getStockQty());
        old.setUpdatedAt();
        return productRepository.save(old);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void delete(Long id) {
        var toDelete = findById(id);
        if (toDelete.isSelled()) {
            throw new EntityValidationException("Um produto que já foi vendido não pode ser excluído");
        }
        productRepository.delete(toDelete);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void changeProductState(Long id) {
        var product = findById(id);
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
}
