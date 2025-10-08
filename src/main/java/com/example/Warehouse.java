package com.example;

import java.math.BigDecimal;
import java.util.*;

// Warehouse class that stores products and enforces singleton-per-name
// Each warehouse is uniquely identified by its nam
public class Warehouse {
    // Static map to store one Warehouse instance per unique name
    // Key: warehouse name, Value: Warehouse instance
    // This ensures that even if multiple parts of the program request a warehouse with the same name
    // they all get the same object (singleton per name)
    private static final Map<String, Warehouse> unique = new HashMap<>();
    private final String name;

    // Map storing all products by their UUID for fast lookup
    private final Map<UUID, Product> products = new HashMap<>();

    // Set of UUIDs for products that have changed (price updates)
    private final Set<UUID> changedProducts = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public Map<UUID, Product> getProducts() {
        return Collections.unmodifiableMap(products);
    }

    public Set<UUID> getChangedProducts() {
        return changedProducts;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        products.put(product.uuid(), product);
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Product product = products.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        product.setPrice(newPrice);
        changedProducts.add(id);
    }
}
