package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public abstract class Product {
    private final UUID id;
    private final String name;
    private final Category category;
    private BigDecimal price;

    protected Product(UUID id, String name, Category category, BigDecimal price) {
        if (id == null) { throw new IllegalArgumentException("ID cannot be null"); }
        if (name == null || name.isBlank()) { throw new IllegalArgumentException("Product name cannot be null or blank");}
        if (category == null) { throw new IllegalArgumentException("Product category cannot be null"); }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("Price cannot be negative."); }

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public UUID uuid() { return id; }
    public String name() { return name; }
    public Category category() { return category; }
    public BigDecimal price() { return price; }

    public void setPrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
        this.price = newPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public abstract String productDetails();
}