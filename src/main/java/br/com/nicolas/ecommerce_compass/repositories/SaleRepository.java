package br.com.nicolas.ecommerce_compass.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nicolas.ecommerce_compass.models.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByCreatedAtBetween(Instant start, Instant end);
}
