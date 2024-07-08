package br.com.nicolas.ecommerce_compass.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.nicolas.ecommerce_compass.dtos.product.ProductRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.product.ProductResponseDTO;
import br.com.nicolas.ecommerce_compass.maps.ProductMapper;
import br.com.nicolas.ecommerce_compass.services.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ProductMapper.fromProductToResponse(productService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll().stream().map(ProductMapper::fromProductToResponse).toList());
    }

    @Transactional
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO product) {
        var newProduct = productService.create(ProductMapper.fromRequestToProduct(product));
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newProduct.getId()).toUri();

        return ResponseEntity.created(uri).body(ProductMapper.fromProductToResponse(newProduct));
    }

    @Transactional
    @PutMapping("/{id}")
    @CacheEvict(value = "products")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id,
            @RequestBody @Valid ProductRequestDTO product) {
        return ResponseEntity.ok(ProductMapper.fromProductToResponse(
                productService.update(ProductMapper.fromRequestToProduct(product), id)));
    }

    @Transactional
    @PutMapping("/changestate/{id}")
    @CacheEvict(value = "products")
    public ResponseEntity<Void> changeState(@PathVariable Long id) {
        productService.changeProductState(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/{id}")
    @CacheEvict(value = "products")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
