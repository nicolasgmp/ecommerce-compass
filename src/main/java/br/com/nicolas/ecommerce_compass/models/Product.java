package br.com.nicolas.ecommerce_compass.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.nicolas.ecommerce_compass.exceptions.EntityValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_products")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Integer stockQty;

    private BigDecimal price;

    private Boolean active;

    @Column(updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SaleItem> items = new ArrayList<>();

    public Product() {
    }

    public Product(String name, Integer stockQty, BigDecimal price) {
        this.setName(name);
        this.setStockQty(stockQty);
        this.setPrice(price);
        this.active = true;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        if (stockQty < 0) {
            throw new EntityValidationException("O estoque não pode ser negativo ou igual a 0");
        }
        this.stockQty = stockQty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price.doubleValue() <= 0) {
            throw new EntityValidationException("O preço não pode ser negativo ou igual a 0");
        }
        this.price = price;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public boolean isSelled() {
        return !items.isEmpty();
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        this.updatedAt = Instant.parse("1970-01-01T00:00:00Z");
    }

    @PreUpdate
    public void onUpdate() {
        this.setUpdatedAt();
    }

    public void subtractStock(Integer quantity) {
        if (this.stockQty != null) {
            if (this.stockQty < quantity) {
                throw new EntityValidationException(
                        "Quantidade a ser vendida deve ser menor ou igual à disponível em estoque");
            }
        }
        this.stockQty -= quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
