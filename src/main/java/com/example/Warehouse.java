package com.example;

import java.util.HashMap;
import java.util.Map;

// Warehouse class that stores products and enforces singleton-per-name
// Each warehouse is uniquely identified by its nam
public class Warehouse {
    // Static map to store one Warehouse instance per unique name
    // Key: warehouse name, Value: Warehouse instance
    // This ensures that even if multiple parts of the program request a warehouse with the same name
    // they all get the same object (singleton per name)
    private static final Map<String, Warehouse> unique = new HashMap<>();
    private final String name;

    // Private constructor ensures controlled instantiation
    private Warehouse(String name) {
        this.name = name;
    }

    // Returns the Warehouse instance for a given name
    // If it already exists, returns the existing instance
    // Otherwise, creates a new Warehouse, stores it, and returns it
    // This ensures only one Warehouse exists per unique name (singleton per name)
    public static Warehouse getInstance(String name) {
        return unique.computeIfAbsent(name, Warehouse::new);
    }


}