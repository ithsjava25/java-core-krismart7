package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FoodProduct extends Product implements Perishable, Shippable {
    // Expiration date of the food product
    private final LocalDate expirationDate;
    // Weight of the food product in kilograms
    private final BigDecimal weight;

    // Constructor for FoodProduct requires the product's name, category, price, expiration date, and weight.
    // The first three parameters are passed to the superclass to initialize shared Product fields,
    // while expirationDate and weight are specific to FoodProduct.
    public FoodProduct(String name, Category category, BigDecimal price, LocalDate expirationDate, BigDecimal weight) {
        // Call the superclass (Product) constructor to initialize the common product fields: name, category, and price
        super(name, category, price);

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be null or negative");
        }
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be null");
        }

        this.expirationDate = expirationDate;
        this.weight = weight;
    }

    @Override
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    // Returns the weight of the food product
    public double getWeight() {
        return weight.doubleValue();
    }
    // Returns a string describing the food product; used for polymorphic behavior
    @Override
    public String productDetails() {
        return "Food: " + getName() + ", Expires: " + getExpirationDate();
    }
    // Calculates shipping cost for the food product based on weight
    @Override
    public BigDecimal calculateShippingCost() {
        return weight.multiply(BigDecimal.valueOf(50));
    }
}
