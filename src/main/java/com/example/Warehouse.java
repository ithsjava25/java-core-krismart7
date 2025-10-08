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
    // Returns the warehouse name
    public String getName() {
        return name;
    }
    // Returns an unmodifiable copy of all products to prevent external modification
    public List<Product> getProducts() {
        return Collections.unmodifiableList(new ArrayList<>(products.values()));
    }
    // Returns a set of IDs for products that had their price updated
    public Set<UUID> getChangedProducts() {
        return Collections.unmodifiableSet(changedProducts);
    }
    // Adds a new product to the warehouse
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        products.put(product.uuid(), product);
    }
    // Retrieves a product by ID, wrapped in Optional to handle missing items safely
    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }
    // Updates the price of a product; if not found, throws an exception
    // Also tracks the updated product’s ID in the changedProducts set
    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Product product = products.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        product.setPrice(newPrice);
        changedProducts.add(id);
    }
    // Returns all perishable products that are expired
    public List<Perishable> expiredProducts() {
        List<Perishable> expiredItems = new ArrayList<>();
        for (Product p : products.values()) {
            if (p instanceof Perishable perishable && perishable.isExpired()) {
                expiredItems.add(perishable);
            }
        }
        return expiredItems;
    }
    // Returns all products that can be shipped
    public List<Shippable> shippableProducts() {
        List<Shippable> items = new ArrayList<>();
        for (Product p : products.values()) {
            if (p instanceof Shippable) {
                items.add((Shippable)p);
            }
        }
        return items;
    }
    // Removes a product from the warehouse and clears its "changed" status
    public void remove(UUID id) {
        products.remove(id);
        changedProducts.remove(id);
    }
    // Removes all products and clears the change tracker
    public void clearProducts() {
        products.clear();
        changedProducts.clear();
    }
    // Returns true if the warehouse has no products
    public boolean isEmpty() {
        return products.isEmpty();
    }
    // Groups all products by category and returns a map of category → product list
    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        Map<Category, List<Product>> grouped = new HashMap<>();
        for (Product p : products.values()) {
            grouped.computeIfAbsent(p.category(), k -> new ArrayList<>()).add(p);
        }
        return grouped;
    }
}
