package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class Product {
    // Unique identifier for the product, assigned automatically
    private final UUID id = UUID.randomUUID();
    // Name of the product, immutable
    private final String name;
    // Category of the product, immutable
    private final Category category;
    // Price of the product, mutable to allow updates via setPrice()
    private BigDecimal price;

    // Protected constructor used by subclasses to initialize a Product with name, category, and price.
    // Automatically assigns a unique ID and ensures required fields are set.
protected Product(String name, Category category, BigDecimal price) {
    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("Product name can't be null or blank");
    }
    if (category == null) {
        throw new IllegalArgumentException("Product category can't be null");
    }
    if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("Price can't be null or negative");
    }

    this.name = name;
    this.category = category;
    this.price = price;
}

    // Returns the unique identifier of this product
    public UUID uuid() {
        return id;
    }
    // Returns the product name (read-only)
    public String getName() {
        return name;
    }
    // Returns the product category (read-only)
    public Category getCategory() {
        return category;
    }
    // Returns the current price of the product
    public BigDecimal getPrice() {
        return price;
    }
    // Updates the product's price, should remain non-null and non-negative
    // Even though the constructor validates the initial price, this method prevents
    // invalid updates after object creation, keeping the Product in a consistent state
    public void setPrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
    this.price = newPrice;
    }

    // Abstract method to be implemented by subclasses, enabling polymorphic behavior.
    // Each subclass provides its own product-specific details, allowing code
    // to work with Product references while invoking the correct implementation at runtime.
    public abstract String productDetails();
}