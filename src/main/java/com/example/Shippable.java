package com.example;

import java.math.BigDecimal;

public interface Shippable {
    // Calculates the shipping cost for the product
    BigDecimal calculateShippingCost();

    // Returns the weight of the product as a double, used in shipping calculations
    double weight();
}