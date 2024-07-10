package br.com.nicolas.ecommerce_compass.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
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

import br.com.nicolas.ecommerce_compass.dtos.product.CreateProductDTO;
import br.com.nicolas.ecommerce_compass.dtos.product.ProductResponseDTO;
import br.com.nicolas.ecommerce_compass.dtos.product.UpdateProductDTO;
import br.com.nicolas.ecommerce_compass.maps.ProductMapper;
import br.com.nicolas.ecommerce_compass.models.Product;
import br.com.nicolas.ecommerce_compass.services.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ProductMapper.fromProductToResponse(productService.findById(id)));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll().stream().map(ProductMapper::fromProductToResponse).toList());
    }

    @Transactional
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid CreateProductDTO product) {
        var newProduct = productService.create(ProductMapper.fromCreateToProduct(product));
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newProduct.getId()).toUri();

        return ResponseEntity.created(uri).body(ProductMapper.fromProductToResponse(newProduct));
    }

    @Transactional
    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable UUID id, @RequestBody @Valid UpdateProductDTO product) {
        var old = productService.findById(id);
        Product newP = new Product(old.getName(), product.stockQty(), product.price());
        return ResponseEntity.ok(ProductMapper.fromProductToResponse(productService.update(newP, id)));
    }

    @Transactional
    @PutMapping("/changestate/{id}")
    public ResponseEntity<Void> changeState(@PathVariable UUID id) {
        productService.changeProductState(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
