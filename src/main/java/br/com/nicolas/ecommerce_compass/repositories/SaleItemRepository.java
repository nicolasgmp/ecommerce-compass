package br.com.nicolas.ecommerce_compass.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nicolas.ecommerce_compass.models.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, UUID> {

}
