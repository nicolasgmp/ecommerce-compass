package br.com.nicolas.ecommerce_compass.services;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.exceptions.EntityValidationException;
import br.com.nicolas.ecommerce_compass.exceptions.InvalidRequestException;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.exceptions.UnauthorizedOperationException;
import br.com.nicolas.ecommerce_compass.models.Product;
import br.com.nicolas.ecommerce_compass.models.Sale;
import br.com.nicolas.ecommerce_compass.models.SaleItem;
import br.com.nicolas.ecommerce_compass.models.User;
import br.com.nicolas.ecommerce_compass.repositories.SaleRepository;
import br.com.nicolas.ecommerce_compass.services.interfaces.ProductService;
import br.com.nicolas.ecommerce_compass.services.interfaces.SaleItemService;
import br.com.nicolas.ecommerce_compass.services.interfaces.SaleService;
import jakarta.transaction.Transactional;

@Service
public class SaleServiceImpl implements SaleService {

    private static final String SALES_NOT_FOUND = "Não foram encontradas vendas cadastradas no banco de dados";
    private static final String WITHOUT_PERMISSION = "Você não tem permissão para visualizar esse recurso";

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleItemService saleItemService;

    @Cacheable(value = "sales", key = "#id")
    public Sale findById(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venda não encontrada no banco de dados para o id: " + id));
        if (!this.hasPermissionToGet(sale)) {
            throw new UnauthorizedOperationException("Não foram encontradas vendas para este usuário");
        }

        return sale;
    }

    @Cacheable(value = "sales", key = "#dayString")
    public List<Sale> findAllByDay(String dayString) {
        Instant start = LocalDate.parse(dayString).atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = LocalDate.parse(dayString).atTime(LocalTime.MAX).atZone(ZoneId.of("UTC")).toInstant();
        var sales = saleRepository.findAllByCreatedAtBetween(start, end);
        this.validateSalesList(sales, SALES_NOT_FOUND + " para este dia");
        var filteredSales = sales.stream()
                .filter(this::hasPermissionToGet)
                .toList();
        this.validateFilteredSales(filteredSales, WITHOUT_PERMISSION);
        return filteredSales;
    }

    @Cacheable(value = "sales", key = "#yearStr + '-' + #monthStr")
    public List<Sale> findAllByMonth(String yearStr, String monthStr) {
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        LocalDate startLD = LocalDate.of(year, month, 1);
        Instant start = startLD.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = startLD.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX).atZone(ZoneId.of("UTC"))
                .toInstant();

        var sales = saleRepository.findAllByCreatedAtBetween(start, end);
        this.validateSalesList(sales, SALES_NOT_FOUND + " para este mês");
        var filteredSales = sales.stream()
                .filter(this::hasPermissionToGet)
                .toList();
        this.validateFilteredSales(filteredSales, WITHOUT_PERMISSION);
        return filteredSales;
    }

    @Cacheable(value = "sales", key = "#weekString")
    public List<Sale> findAllByWeek(String weekString) {
        LocalDate startLD = LocalDate.parse(weekString);
        Instant start = startLD.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = startLD.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX).atZone(ZoneId.of("UTC")).toInstant();

        var sales = saleRepository.findAllByCreatedAtBetween(start, end);
        this.validateSalesList(sales, SALES_NOT_FOUND + " para esta semana");
        var filteredSales = sales.stream()
                .filter(this::hasPermissionToGet)
                .toList();
        this.validateFilteredSales(filteredSales, WITHOUT_PERMISSION);
        return filteredSales;
    }

    @Cacheable(value = "sales", key = "'all'")
    public List<Sale> findAll() {
        var sales = saleRepository.findAll();
        this.validateSalesList(sales, SALES_NOT_FOUND);
        var filteredSales = sales.stream()
                .filter(this::hasPermissionToGet)
                .toList();
        this.validateFilteredSales(filteredSales, WITHOUT_PERMISSION);
        return filteredSales;
    }

    @Transactional
    @CacheEvict(value = "sales", allEntries = true)
    public Sale create(Sale sale) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setItems(sale);
        if (sale.getItems().isEmpty()) {
            throw new EntityValidationException(
                    "Uma venda não pode ser finalizada sem ao menos um produto a ser comprado");
        }
        sale.setTotal(this.calculateTotalValue(sale));
        sale.setUser((User) auth.getPrincipal());
        return saleRepository.save(sale);
    }

    @Transactional
    @CacheEvict(value = "sales", allEntries = true)
    public Sale update(UUID id, Sale sale) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var saleDB = this.findById(id);
        if (!auth.getName().equals(saleDB.getUser().getEmail())) {
            throw new InvalidRequestException("Apenas o mesmo usuário pode alterar suas vendas");
        }
        this.mergeItems(saleDB, sale);
        saleDB.setUpdatedAt();
        saleDB.setTotal(this.calculateTotalValue(saleDB));
        return saleRepository.save(saleDB);
    }

    @Transactional
    @CacheEvict(value = "sales", allEntries = true)
    public void delete(UUID id) {
        var sale = this.findById(id);
        saleItemService.deleteItemsFromSale(sale.getItems());
        saleRepository.delete(sale);
    }

    @CacheEvict(value = "sales", allEntries = true)
    public void clearSalesCache() {
    }

    private void mergeItems(Sale saleDB, Sale sale) {
        for (SaleItem newItem : sale.getItems()) {
            Product product = productService.findById(newItem.getProductId());
            if (!product.isActive()) {
                throw new EntityValidationException("Um produto inativo não pode ser vendido");
            }

            saleDB.getItems().stream()
                    .filter(itemDB -> itemDB.getProduct().getId().equals(newItem.getProductId()))
                    .findFirst()
                    .ifPresentOrElse(
                            itemDB -> {
                                itemDB.setQuantity(itemDB.getQuantity() + newItem.getQuantity());
                                productService.subtractStock(newItem.getQuantity(), product);
                            },
                            () -> {
                                this.validateAndProcessItem(newItem, saleDB);
                                saleDB.getItems().add(newItem);
                            });
        }
    }

    private void setItems(Sale sale) {
        sale.getItems().stream().forEach(item -> this.validateAndProcessItem(item, sale));
    }

    private void validateAndProcessItem(SaleItem item, Sale sale) {
        if (item.getQuantity() <= 0) {
            throw new EntityValidationException("A quantidade não pode ser negativa ou igual a 0");
        }

        Product product = productService.findById(item.getProductId());
        if (!product.isActive()) {
            throw new EntityValidationException("Um produto inativo não pode ser vendido");
        }

        productService.subtractStock(item.getQuantity(), product);
        item.setProduct(product);
        item.setProductId(product.getId());
        item.setSale(sale);
    }

    private BigDecimal calculateTotalValue(Sale sale) {
        return sale.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private void validateSalesList(List<Sale> sales, String msg) {
        if (sales.isEmpty()) {
            throw new ResourceNotFoundException(msg);
        }
    }

    private void validateFilteredSales(List<Sale> filtered, String msg) {
        if (filtered.isEmpty()) {
            throw new UnauthorizedOperationException(msg);
        }
    }

    private boolean hasPermissionToGet(Sale sale) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        boolean isAdm = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        boolean isOwner = sale.getUser().getEmail().equals(email);

        return isAdm || isOwner;
    }

}
