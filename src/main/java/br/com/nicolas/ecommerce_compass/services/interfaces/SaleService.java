package br.com.nicolas.ecommerce_compass.services.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.nicolas.ecommerce_compass.models.Sale;

public interface SaleService {

    Sale findById(UUID id);

    List<Sale> findAllByDay(String dayString);

    List<Sale> findAllByMonth(String yearStr, String monthStr);

    List<Sale> findAllByWeek(String weekString);

    List<Sale> findAll();

    Sale create(Sale sale);

    Sale update(UUID id, Sale sale);

    void delete(UUID id);

    void clearSalesCache();
}
