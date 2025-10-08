package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class Product {
    // Unique identifier for the product
    private final UUID id = UUID.randomUUID();
    // Name of the product
    private final String name;
    // Category of the product
    private final Category category;
    // Price of the product, intentionally mutable to allow updates via setPrice()
    private BigDecimal price;

    // Protected constructor used by subclasses to initialize a Product with name, category, and price.
    // Automatically assigns a unique ID and ensures required fields are set.
protected Product(String name, Category category, BigDecimal price) {
    this.name = name;
    this.category = category;
    this.price = price;
}


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
    this.price = price;
    }

    // Abstract method to return product-specific details
    public abstract String productDetails();
}