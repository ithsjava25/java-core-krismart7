package com.example;

import java.time.LocalDate;

public interface Perishable {

    LocalDate expirationDate();
    default boolean isExpired() {
        return !expirationDate().isAfter(LocalDate.now());
    }
}