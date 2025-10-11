package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    private final int warrantyMonths;
    private final BigDecimal weight;

    public ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, int warrantyMonths, BigDecimal weight) {
        super(id, name, category, price);

        if (warrantyMonths < 0) { throw new IllegalArgumentException("Warranty months cannot be negative."); }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) < 0) { throw new IllegalArgumentException("Weight cannot be null or negative"); }

        this.warrantyMonths = warrantyMonths;
        this.weight = weight;
    }

    @Override
    public double weight() { return weight.doubleValue(); }

    @Override
    public String productDetails() { return "Electronics: " + name() + ", Warranty: " + warrantyMonths + " months"; }

    @Override
    public BigDecimal calculateShippingCost() {
        BigDecimal cost = BigDecimal.valueOf(79);
        if (weight.compareTo(BigDecimal.valueOf(5.0)) > 0) {
            cost = cost.add(BigDecimal.valueOf(49));
        }
        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "ElectronicsProduct{" + "warrantyMonths=" + warrantyMonths + ", weight=" + weight + '}';
    }
}