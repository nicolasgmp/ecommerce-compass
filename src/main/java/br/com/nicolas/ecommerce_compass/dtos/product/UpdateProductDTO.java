package br.com.nicolas.ecommerce_compass.dtos.product;

import java.math.BigDecimal;

public record UpdateProductDTO(Integer stockQty, BigDecimal price) {

}
