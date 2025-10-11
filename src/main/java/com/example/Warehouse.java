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
    private final Map<UUID, Product> productsById = new HashMap<>();

    // Set of UUIDs for products that have changed (price updates)
    private final Set<UUID> updatedProductIds = new HashSet<>();

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
    // Returns the warehouse name
    public String getName() {
        return name;
    }
    // Returns an unmodifiable copy of all products to prevent external modification
    public List<Product> getProducts() {
        return Collections.unmodifiableList(new ArrayList<>(productsById.values()));
    }
    // Returns a set of IDs for products that had their price updated
    public Set<UUID> getUpdatedProductIds() {
        return Collections.unmodifiableSet(updatedProductIds);
    }
    // Adds a new product to the warehouse
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        System.out.println("Adding product: " + product.name() + " with price " + product.price());
        productsById.put(product.uuid(), product);
        System.out.println("Warehouse size now: " + productsById.size());
    }
    // Retrieves a product by ID, wrapped in Optional to handle missing items safely
    public Optional<Product> getProductsById(UUID id) {
        return Optional.ofNullable(productsById.get(id));
    }
    // Updates the price of a product; if not found, throws an exception
    // Also tracks the updated product’s ID in the changedProducts set
    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Product product = productsById.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        product.setPrice(newPrice);
        updatedProductIds.add(id);
    }
    // Returns all perishable products that are expired
    public List<Perishable> expiredProducts() {
        List<Perishable> expiredProducts = new ArrayList<>();
        for (Product p : productsById.values()) {
            if (p instanceof Perishable perishable && perishable.isExpired()) {
                expiredProducts.add(perishable);
            }
        }
        return expiredProducts;
    }
    // Returns all products that can be shipped
    public List<Shippable> shippableProducts() {
        List<Shippable> shippableProducts = new ArrayList<>();
        for (Product p : productsById.values()) {
            if (p instanceof Shippable shippable) {
                shippableProducts.add(shippable);
            }
        }
        return shippableProducts;
    }
    // Removes a product from the warehouse and clears its "changed" status
    public void remove(UUID id) {
        productsById.remove(id);
        updatedProductIds.remove(id);
    }
    // Removes all products and clears the change tracker
    public void clearProducts() {
        productsById.clear();
        updatedProductIds.clear();
    }
    // Returns true if the warehouse has no products
    public boolean isEmpty() {
        return productsById.isEmpty();
    }
    // Groups all products by category and returns a map of category → product list
    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        Map<Category, List<Product>> grouped = new HashMap<>();
        for (Product p : productsById.values()) {
            grouped.computeIfAbsent(p.category(), k -> new ArrayList<>()).add(p);
        }
        return grouped;
    }
}
