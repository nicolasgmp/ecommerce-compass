package br.com.nicolas.ecommerce_compass.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.nicolas.ecommerce_compass.dtos.sale.SaleRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.sale.SaleResponseDTO;
import br.com.nicolas.ecommerce_compass.maps.SaleMapper;
import br.com.nicolas.ecommerce_compass.services.SaleService;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<SaleResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(SaleMapper.fromSaleToResponse(saleService.findById(id)));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<SaleResponseDTO>> findAll() {
        return ResponseEntity.ok(saleService.findAll().stream().map(SaleMapper::fromSaleToResponse).toList());
    }

    @GetMapping(value = "/date", produces = "application/json")
    public ResponseEntity<List<SaleResponseDTO>> findAllByDay(@RequestParam("date") String date) {
        return ResponseEntity.ok(saleService.findAllByDay(date).stream()
                .map(SaleMapper::fromSaleToResponse).toList());
    }

    @GetMapping(value = "/report/monthly", produces = "application/json")
    public ResponseEntity<List<SaleResponseDTO>> findAllByMonth(
            @RequestParam String year, @RequestParam String month) {
        return ResponseEntity.ok(saleService.findAllByMonth(year, month).stream()
                .map(SaleMapper::fromSaleToResponse).toList());
    }

    @GetMapping(value = "/report/weekly", produces = "application/json")
    public ResponseEntity<List<SaleResponseDTO>> findAllByWeek(@RequestParam("date") String date) {
        return ResponseEntity.ok(saleService.findAllByWeek(date).stream()
                .map(SaleMapper::fromSaleToResponse).toList());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO sale) {
        var newSale = saleService.create(SaleMapper.fromRequestToSale(sale));
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newSale.getId()).toUri();

        return ResponseEntity.created(uri).body(SaleMapper.fromSaleToResponse(newSale));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> update(@PathVariable UUID id, @RequestBody SaleRequestDTO sale) {
        var updated = saleService.update(id, SaleMapper.fromRequestToSale(sale));
        return ResponseEntity.ok().body(SaleMapper.fromSaleToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
