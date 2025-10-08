package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    // Warranty period in months; must be non-negative
    private final int warrantyMonths;
    // Weight of the product in kilograms; must be non-negative
    private final BigDecimal weight;

    // Constructs an ElectronicsProduct with the specified parameters
    // Calls the superclass constructor to initialize common Product fields
    public ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, int warrantyMonths, BigDecimal weight) {
        super(name, category, price);

        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be null or negative");
        }

        this.warrantyMonths = warrantyMonths;
        this.weight = weight;
    }
    // Returns the warranty period in months
    public int getWarrantyMonths() {
        return warrantyMonths;
    }
    // Returns the weight of the product
    public double weight() {
        return weight.doubleValue();
    }
    // Provides product-specific details, demonstrating polymorphism
    @Override
    public String productDetails() {
        return "Electronics: " + name() + ", Warranty: " + warrantyMonths + " months";
    }
    // Calculates shipping cost for the electronics product
    @Override
    public BigDecimal calculateShippingCost() {
        BigDecimal cost = BigDecimal.valueOf(79); // base cost
        if (weight.compareTo(BigDecimal.valueOf(5.0)) > 0) {
            cost = cost.add(BigDecimal.valueOf(49));
        }
        return cost;
    }
}