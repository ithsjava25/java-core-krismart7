package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

public class FoodProduct extends Product implements Perishable, Shippable {
    private final LocalDate expirationDate;
    private final BigDecimal weight;

    public FoodProduct(UUID id, String name, Category category, BigDecimal price, LocalDate expirationDate, BigDecimal weight) {
        super(id, name, category, price);

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("Price cannot be negative."); }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("Weight cannot be negative."); }
        if (expirationDate == null) { throw new IllegalArgumentException("Expiration date cannot be null."); }

        this.expirationDate = expirationDate;
        this.weight = weight;
    }

    @Override
    public LocalDate expirationDate() { return expirationDate; }
    @Override

    public double weight() { return weight.doubleValue(); }

    @Override
    public String productDetails() { return "Food: " + name() + ", Expires: " + expirationDate(); }

    @Override
    public BigDecimal calculateShippingCost() {
        return weight.multiply(BigDecimal.valueOf(50)).setScale(2, RoundingMode.HALF_UP);
    }
}