package com.example;

import java.time.LocalDate;

public interface Perishable {
    // Returns the expiration date of the food product
    LocalDate expirationDate();

    // Returns true if the product is expired, default implementation based on current date
    default boolean isExpired() {
        return !expirationDate().isAfter(LocalDate.now());
    }
}