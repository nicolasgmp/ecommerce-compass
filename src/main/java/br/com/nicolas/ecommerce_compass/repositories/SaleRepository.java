package br.com.nicolas.ecommerce_compass.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nicolas.ecommerce_compass.models.Sale;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
    List<Sale> findAllByCreatedAtBetween(Instant start, Instant end);
}
